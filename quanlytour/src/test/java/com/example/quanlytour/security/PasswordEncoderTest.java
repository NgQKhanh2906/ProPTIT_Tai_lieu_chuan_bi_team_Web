package com.example.quanlytour.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderTest {

    private static final String RAW_PASSWORD = "Test@12345";

    private final PasswordEncoder passwordEncoder = new SecurityConfig(null, null, null).passwordEncoder();

    @Test
    void newHashHasBcryptPrefix() {
        String encoded = passwordEncoder.encode(RAW_PASSWORD);

        System.out.println("Hash moi: " + encoded + " (" + encoded.length() + " ky tu)");
        assertThat(encoded).startsWith("{bcrypt}$2a$10$").hasSize(68);
        assertThat(passwordEncoder.matches(RAW_PASSWORD, encoded)).isTrue();
        assertThat(passwordEncoder.upgradeEncoding(encoded)).isFalse();
    }

    @Test
    void oldHashWithoutPrefixStillMatchesAndNeedsUpgrade() {
        String oldHash = new BCryptPasswordEncoder().encode(RAW_PASSWORD);

        System.out.println("Hash cu: " + oldHash + " (" + oldHash.length() + " ky tu)");
        System.out.println("matches = " + passwordEncoder.matches(RAW_PASSWORD, oldHash)
                + ", upgradeEncoding = " + passwordEncoder.upgradeEncoding(oldHash));
        assertThat(passwordEncoder.matches(RAW_PASSWORD, oldHash)).isTrue();
        assertThat(passwordEncoder.upgradeEncoding(oldHash)).isTrue();
    }

    @Test
    void missingDefaultEncoderRejectsOldHash() {
        DelegatingPasswordEncoder withoutDefault = new DelegatingPasswordEncoder(
                "bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));
        String oldHash = new BCryptPasswordEncoder().encode(RAW_PASSWORD);

        Throwable thrown = catchThrowable(() -> withoutDefault.matches(RAW_PASSWORD, oldHash));

        System.out.println("Quen setDefaultPasswordEncoderForMatches: "
                + thrown.getClass().getName() + ": " + thrown.getMessage());
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void bcryptOnlyChecksFirst72Bytes() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String password72 = "a".repeat(72);
        String password73 = "a".repeat(73);

        Throwable thrown = catchThrowable(() -> bcrypt.encode(password73));
        boolean matches = bcrypt.matches(password73, bcrypt.encode(password72));

        System.out.println("encode 73 byte: " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage());
        System.out.println("matches(73 chu a, hash cua 72 chu a) = " + matches);
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        assertThat(matches).isTrue();
    }

}
