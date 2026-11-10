package com.example.quanlytour.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.AEADBadTagException;
import org.junit.jupiter.api.Test;

class AesGcmEncryptorTest {

    private static final String PHONE = "0912345678";

    private static String randomKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    @Test
    void encryptTwiceGivesDifferentResultsAndDecryptsBack() {
        AesGcmEncryptor encryptor = new AesGcmEncryptor(randomKey());

        String first = encryptor.encrypt(PHONE);
        String second = encryptor.encrypt(PHONE);

        System.out.println("Lan 1: " + first + " (" + first.length() + " ky tu)");
        System.out.println("Lan 2: " + second + " (" + second.length() + " ky tu)");
        System.out.println("Giai ma lan 1: " + encryptor.decrypt(first));
        System.out.println("Giai ma lan 2: " + encryptor.decrypt(second));
        assertThat(first).isNotEqualTo(second).hasSize(52);
        assertThat(second).hasSize(52);
        assertThat(encryptor.decrypt(first)).isEqualTo(PHONE);
        assertThat(encryptor.decrypt(second)).isEqualTo(PHONE);
    }

    @Test
    void tamperedDataIsRejected() {
        AesGcmEncryptor encryptor = new AesGcmEncryptor(randomKey());
        byte[] stored = Base64.getDecoder().decode(encryptor.encrypt(PHONE));
        stored[15] ^= 1;
        String tampered = Base64.getEncoder().encodeToString(stored);

        Throwable thrown = catchThrowable(() -> encryptor.decrypt(tampered));

        System.out.println("Sua 1 bit: " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage()
                + " (nguyen nhan: " + thrown.getCause().getClass().getName() + ")");
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasMessage("Khong giai ma duoc du lieu")
                .hasCauseInstanceOf(AEADBadTagException.class);
    }

    @Test
    void wrongKeyIsRejected() {
        String encrypted = new AesGcmEncryptor(randomKey()).encrypt(PHONE);
        AesGcmEncryptor otherEncryptor = new AesGcmEncryptor(randomKey());

        Throwable thrown = catchThrowable(() -> otherEncryptor.decrypt(encrypted));

        System.out.println("Sai khoa: " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage()
                + " (nguyen nhan: " + thrown.getCause().getClass().getName() + ")");
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(AEADBadTagException.class);
    }

    @Test
    void keyWithWrongLengthIsRejected() {
        String shortKey = Base64.getEncoder().encodeToString(new byte[16]);

        Throwable thrown = catchThrowable(() -> new AesGcmEncryptor(shortKey));

        System.out.println("Khoa 16 byte: " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage());
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasMessage("app.encryption.key phai la chuoi Base64 cua dung 32 byte");
    }

}
