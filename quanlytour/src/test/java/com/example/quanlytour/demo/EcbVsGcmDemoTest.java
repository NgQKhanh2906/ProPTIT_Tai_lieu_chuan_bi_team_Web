package com.example.quanlytour.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HexFormat;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.junit.jupiter.api.Test;

class EcbVsGcmDemoTest {

    private static final byte[] TWO_EQUAL_BLOCKS = "0911111111-KHACH0911111111-KHACH".getBytes(StandardCharsets.UTF_8);

    @Test
    void ecbLeaksRepeatedBlocksButGcmDoesNot() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();

        Cipher defaultCipher = Cipher.getInstance("AES");
        defaultCipher.init(Cipher.ENCRYPT_MODE, key);
        boolean ecbEqual = printBlocks("AES mac dinh (ECB)", defaultCipher.doFinal(TWO_EQUAL_BLOCKS));

        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        Cipher gcmCipher = Cipher.getInstance("AES/GCM/NoPadding");
        gcmCipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
        boolean gcmEqual = printBlocks("AES/GCM/NoPadding ", gcmCipher.doFinal(TWO_EQUAL_BLOCKS));

        assertThat(ecbEqual).isTrue();
        assertThat(gcmEqual).isFalse();
    }

    private boolean printBlocks(String label, byte[] cipherText) {
        HexFormat hex = HexFormat.of();
        String block1 = hex.formatHex(cipherText, 0, 16);
        String block2 = hex.formatHex(cipherText, 16, 32);
        System.out.println(label + " khoi 1: " + block1);
        System.out.println(label + " khoi 2: " + block2);
        System.out.println(label + " hai khoi giong nhau: " + block1.equals(block2));
        return block1.equals(block2);
    }

}
