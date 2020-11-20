package com.teasoft.passman.persistence;

import com.teasoft.passman.crypto.Crypto;

public interface Persistence<T> {
    void save(T data, String filePath, Boolean applyCrypto, Crypto crypto);

    void load(T data, String filepath, Boolean applyCrypto, Crypto crypto);
}
