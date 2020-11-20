package com.teasoft.passman.crypto;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.io.OutputStream;

public interface Crypto {

    byte[] getIv();

    void setIv(byte[] iv);

    byte[] getSalt();

    void setSalt(byte[] salt);

    Cipher getEncryptionCipher();

    Cipher getDecryptionCipher();

    byte[] encrypt(byte[] data);

    byte[] encryptPadWithIvAndSalt(byte[] data);

    byte[] decrypt(byte[] data);

    byte[] decryptPadWithIvAndSalt(byte[] data);

    void writeIvAndSalt(OutputStream outputStream);

    void readIvAndSalt(InputStream inputStream);
}
