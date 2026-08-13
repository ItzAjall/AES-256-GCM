package com.daniyal.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Aes256 {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;

    private static final SecureRandom RANDOM = new SecureRandom();

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        RANDOM.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        GCMParameterSpec spec =
                new GCMParameterSpec(TAG_SIZE, iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] ciphertext =
                cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        byte[] result = new byte[iv.length + ciphertext.length];

        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(result);
    }

    public static String decrypt(String encrypted, SecretKey key) throws Exception {
        byte[] data = Base64.getDecoder().decode(encrypted);

        if (data.length < IV_SIZE + 16) {
            throw new IllegalArgumentException("Invalid encrypted data");
        }

        byte[] iv = new byte[IV_SIZE];
        byte[] ciphertext = new byte[data.length - IV_SIZE];

        System.arraycopy(data, 0, iv, 0, IV_SIZE);
        System.arraycopy(data, IV_SIZE, ciphertext, 0, ciphertext.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        GCMParameterSpec spec =
                new GCMParameterSpec(TAG_SIZE, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return new String(plaintext, StandardCharsets.UTF_8);
    }
}