package com.lkup.accounts.utilities;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class SpringEncryptorUtil {
    private static final String PASSWORD = "encryptionPassword"; // ideally from env
    private static final String SALT = "a1b2c3d4e5f6g7h8";         // 16-character hex

    private static final TextEncryptor encryptor =
            Encryptors.text(PASSWORD, SALT);

    public static String encrypt(String value) {
        return encryptor.encrypt(value);
    }

    public static String decrypt(String encryptedValue) {
        return encryptor.decrypt(encryptedValue);
    }
}
