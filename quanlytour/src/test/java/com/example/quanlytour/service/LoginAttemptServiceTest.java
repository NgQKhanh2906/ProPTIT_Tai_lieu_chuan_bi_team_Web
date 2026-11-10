package com.example.quanlytour.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class LoginAttemptServiceTest {

    private static final Instant START = Instant.parse("2026-11-02T08:00:00Z");

    private static final class MutableClock extends Clock {

        private Instant now;

        private MutableClock(Instant start) {
            this.now = start;
        }

        private void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }

    }

    @Test
    void locksAfterFiveFailuresForFifteenMinutes() {
        MutableClock clock = new MutableClock(START);
        LoginAttemptService service = new LoginAttemptService(clock);

        for (int attempt = 1; attempt <= 5; attempt++) {
            service.loginFailed("tv1");
            System.out.println("Sai lan " + attempt + " -> isBlocked = " + service.isBlocked("tv1"));
        }
        assertThat(service.isBlocked("tv1")).isTrue();

        clock.advance(Duration.ofMinutes(14).plusSeconds(59));
        System.out.println("Sau 14 phut 59 giay -> isBlocked = " + service.isBlocked("tv1"));
        assertThat(service.isBlocked("tv1")).isTrue();

        clock.advance(Duration.ofSeconds(1));
        System.out.println("Sau dung 15 phut -> isBlocked = " + service.isBlocked("tv1"));
        assertThat(service.isBlocked("tv1")).isFalse();
    }

    @Test
    void usernameIsCaseInsensitive() {
        LoginAttemptService service = new LoginAttemptService(new MutableClock(START));

        for (int attempt = 1; attempt <= 5; attempt++) {
            service.loginFailed(" TV1 ");
        }

        System.out.println("Sai 5 lan voi \" TV1 \" -> isBlocked(\"tv1\") = " + service.isBlocked("tv1"));
        assertThat(service.isBlocked("tv1")).isTrue();
    }

    @Test
    void successResetsCounter() {
        LoginAttemptService service = new LoginAttemptService(new MutableClock(START));

        for (int attempt = 1; attempt <= 4; attempt++) {
            service.loginFailed("tv3");
        }
        service.loginSucceeded("tv3");
        service.loginFailed("tv3");

        System.out.println("Sai 4 lan, dung 1 lan, sai 1 lan -> isBlocked = " + service.isBlocked("tv3"));
        assertThat(service.isBlocked("tv3")).isFalse();
    }

    @Test
    void oldFailuresExpireAfterFifteenMinutes() {
        MutableClock clock = new MutableClock(START);
        LoginAttemptService service = new LoginAttemptService(clock);

        for (int attempt = 1; attempt <= 4; attempt++) {
            service.loginFailed("tv2");
        }
        clock.advance(Duration.ofMinutes(16));
        service.loginFailed("tv2");

        System.out.println("Sai 4 lan, 16 phut sau sai them 1 lan -> isBlocked = " + service.isBlocked("tv2"));
        assertThat(service.isBlocked("tv2")).isFalse();
    }

}
