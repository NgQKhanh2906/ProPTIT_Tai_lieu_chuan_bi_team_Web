package com.example.quanlytour.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.security.JwtService;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class Base64DemoTest {

    @Test
    void base64AndJwtPayloadCanBeReadWithoutKey() {
        String phone = "0911111111";
        String encoded = Base64.getEncoder().encodeToString(phone.getBytes(StandardCharsets.UTF_8));
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);

        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        JwtService jwtService = new JwtService(Base64.getEncoder().encodeToString(secret), 3600000);
        User user = new User();
        user.setUsername("tv1");
        user.setRole(Role.USER);
        String token = jwtService.generateToken(user);
        String payload = token.split("\\.")[1];
        String json = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);

        System.out.println("So dien thoai     : " + phone);
        System.out.println("Base64 encode     : " + encoded);
        System.out.println("Base64 decode     : " + decoded);
        System.out.println("Payload JWT (giai ma khong can jwt.secret): " + json);
        assertThat(decoded).isEqualTo(phone);
        assertThat(json).contains("\"sub\":\"tv1\"");
    }

}
