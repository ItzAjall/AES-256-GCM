package com.daniyal.service;

import com.daniyal.dto.*;
import com.daniyal.exception.InvalidEncryptionException;
import com.daniyal.util.Aes256;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    public KeyResponse generateKey() throws Exception {
        SecretKey key = Aes256.generateKey();

        return new KeyResponse(Base64.getEncoder()
                .encodeToString(key.getEncoded()));
    }

    public EncryptResponse encrypt(EncryptRequest encryptRequest) throws Exception {
        SecretKey key = keyFromBase64(encryptRequest.key());

        return new EncryptResponse(Aes256.encrypt(encryptRequest.text(), key));
    }

    public DecryptResponse decrypt(DecryptRequest decryptRequest) {
        try {
            SecretKey key = keyFromBase64(decryptRequest.key());

            return new DecryptResponse(
                    Aes256.decrypt(decryptRequest.encrypted(), key)
            );

        } catch (Exception e) {
            throw new InvalidEncryptionException(
                    "Invalid key or encrypted data"
            );
        }
    }

    private SecretKey keyFromBase64(String keyBase64) {
        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(keyBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 key");
        }

        if (keyBytes.length != 32) {
            throw new IllegalArgumentException(
                    "AES-256 key must be exactly 32 bytes"
            );
        }

        return new SecretKeySpec(keyBytes, "AES");
    }
}