package com.example.quanlytour.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesGcmEncryptor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_LENGTH_BYTES = 32;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;
    private static final String INVALID_KEY_MESSAGE = "app.encryption.key phai la chuoi Base64 cua dung 32 byte";

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public AesGcmEncryptor(@Value("${app.encryption.key}") String base64Key) {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(INVALID_KEY_MESSAGE, exception);
        }
        if (keyBytes.length != KEY_LENGTH_BYTES) {
            throw new IllegalStateException(INVALID_KEY_MESSAGE);
        }
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String encrypt(String plainText) {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        secureRandom.nextBytes(iv);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] cipherTextWithTag = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] stored = ByteBuffer.allocate(iv.length + cipherTextWithTag.length)
                    .put(iv)
                    .put(cipherTextWithTag)
                    .array();
            return Base64.getEncoder().encodeToString(stored);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Khong ma hoa duoc du lieu", exception);
        }
    }

    public String decrypt(String encryptedText) {
        byte[] stored;
        try {
            stored = Base64.getDecoder().decode(encryptedText);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Du lieu ma hoa khong hop le", exception);
        }
        if (stored.length <= IV_LENGTH_BYTES) {
            throw new IllegalStateException("Du lieu ma hoa khong hop le");
        }
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, stored, 0, IV_LENGTH_BYTES));
            byte[] plainBytes = cipher.doFinal(stored, IV_LENGTH_BYTES, stored.length - IV_LENGTH_BYTES);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Khong giai ma duoc du lieu", exception);
        }
    }

}