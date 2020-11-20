package com.teasoft.passman.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

// Based on:
// https://wiki.sei.cmu.edu/confluence/display/java/MSC61-J.+Do+not+use+insecure+or+weak+cryptographic+algorithms

public class AesGcmCrypto implements Crypto {
    private static final String AES_ALGORITHM = "AES";
    private static final String ALGORITHM_INSTANCE = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BIT_LENGTH = 128; // must be one of {128, 120, 112, 104, 96}
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    public static String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static int KEY_LENGTH = 256;
    public static int ITERATION_COUNT = 3713;
    private final char[] password;
    private SecretKey secretKey;
    private byte[] iv;
    private byte[] salt;
    private Cipher cipher;

    public AesGcmCrypto(char[] password) {
        this.password = password;
    }

    private static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    @SuppressWarnings("unused")
    private static SecretKey getAESKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AesGcmCrypto.AES_ALGORITHM);
            keyGen.init(AesGcmCrypto.KEY_LENGTH, SecureRandom.getInstanceStrong());
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKey getAESKey(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(AesGcmCrypto.SECRET_KEY_FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, salt, AesGcmCrypto.ITERATION_COUNT, AesGcmCrypto.KEY_LENGTH);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(),
                AesGcmCrypto.AES_ALGORITHM);
    }

    @Override
    public byte[] getIv() {
        return this.iv;
    }

    @Override
    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    @Override
    public byte[] getSalt() {
        return this.salt;
    }

    @Override
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    @Override
    public Cipher getEncryptionCipher() {
        try {
            // Never reuse same IV and salt. Always regenerate.
            this.iv = AesGcmCrypto.getRandomNonce(AesGcmCrypto.IV_LENGTH_BYTE);
            this.salt = AesGcmCrypto.getRandomNonce(AesGcmCrypto.SALT_LENGTH_BYTE);
            this.secretKey = AesGcmCrypto.getAESKey(this.password, this.salt);
            this.cipher = Cipher.getInstance(AesGcmCrypto.ALGORITHM_INSTANCE);
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey,
                    new GCMParameterSpec(AesGcmCrypto.GCM_TAG_BIT_LENGTH, this.iv));
            return this.cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cipher getDecryptionCipher() {
        try {
            // IV and Salt must be read or set before getting the decryption cipher
            this.secretKey = AesGcmCrypto.getAESKey(this.password, this.salt);
            this.cipher = Cipher.getInstance(AesGcmCrypto.ALGORITHM_INSTANCE);
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey,
                    new GCMParameterSpec(AesGcmCrypto.GCM_TAG_BIT_LENGTH, this.iv));
            return this.cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            this.cipher = this.getEncryptionCipher();
            return this.cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encryptPadWithIvAndSalt(byte[] data) {
        byte[] cipherText = this.encrypt(data);
        return ByteBuffer.allocate(this.iv.length + this.salt.length + cipherText.length).put(this.iv).put(this.salt)
                .put(cipherText).array();
    }

    @Override
    public byte[] decrypt(byte[] data) {
        try {
            this.cipher = this.getDecryptionCipher();
            return this.cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decryptPadWithIvAndSalt(byte[] data) {
        try {
            ByteBuffer bb = ByteBuffer.wrap(data);

            byte[] iv = new byte[AesGcmCrypto.IV_LENGTH_BYTE];
            bb.get(iv);

            byte[] salt = new byte[AesGcmCrypto.SALT_LENGTH_BYTE];
            bb.get(salt);

            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);

            this.cipher = this.getDecryptionCipher();
            return this.cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeIvAndSalt(OutputStream outputStream) {
        try {
            outputStream.write(this.getIv());
            outputStream.write(this.getSalt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readIvAndSalt(InputStream inputStream) {
        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            if (inputStream.read(iv, 0, IV_LENGTH_BYTE) != IV_LENGTH_BYTE)
                throw new RuntimeException("Invalid data to decrypt");
            this.setIv(iv);
            byte[] salt = new byte[SALT_LENGTH_BYTE];
            if (inputStream.read(salt, 0, SALT_LENGTH_BYTE) != SALT_LENGTH_BYTE)
                throw new RuntimeException("Invalid data to decrypt");
            this.setSalt(salt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
