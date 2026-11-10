package com.example.quanlytour.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptService() {
        this(Clock.systemUTC());
    }

    LoginAttemptService(Clock clock) {
        this.clock = clock;
    }

    public boolean isBlocked(String username) {
        Instant now = clock.instant();
        Attempt attempt = attempts.computeIfPresent(toKey(username),
                (key, current) -> current.isExpiredAt(now) ? null : current);
        return attempt != null && attempt.isLockedAt(now);
    }

    public void loginFailed(String username) {
        Instant now = clock.instant();
        attempts.compute(toKey(username), (key, current) -> {
            if (current == null || current.isExpiredAt(now)) {
                return new Attempt(1, now, null);
            }
            int failures = current.failures() + 1;
            Instant lockedUntil = failures >= MAX_FAILED_ATTEMPTS ? now.plus(LOCK_DURATION) : null;
            return new Attempt(failures, current.firstFailureAt(), lockedUntil);
        });
    }

    public void loginSucceeded(String username) {
        attempts.remove(toKey(username));
    }

    private String toKey(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }

    private record Attempt(int failures, Instant firstFailureAt, Instant lockedUntil) {

        boolean isLockedAt(Instant now) {
            return lockedUntil != null && now.isBefore(lockedUntil);
        }

        boolean isExpiredAt(Instant now) {
            if (lockedUntil != null) {
                return !now.isBefore(lockedUntil);
            }
            return !now.isBefore(firstFailureAt.plus(LOCK_DURATION));
        }

    }

}