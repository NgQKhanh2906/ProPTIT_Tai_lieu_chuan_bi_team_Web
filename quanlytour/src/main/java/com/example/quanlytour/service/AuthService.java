package com.example.quanlytour.service;

import com.example.quanlytour.dto.AuthResponse;
import com.example.quanlytour.dto.LoginRequest;
import com.example.quanlytour.dto.RegisterRequest;
import com.example.quanlytour.dto.UserResponse;
import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.KhachHangRepository;
import com.example.quanlytour.repository.UserRepository;
import com.example.quanlytour.security.JwtService;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";
    private static final int MAX_PASSWORD_BYTES = 72;

    private final UserRepository userRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String username = request.username();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Ten dang nhap da ton tai");
        }
        if (request.password().getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
            throw new IllegalStateException("Mat khau vuot qua 72 byte");
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setCustomerName(request.customerName().trim());
        khachHang.setPhone(request.phone());
        khachHang.setEmail(request.email());
        khachHangRepository.save(khachHang);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setKhachHang(khachHang);
        return UserResponse.from(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        return new AuthResponse(accessToken, TOKEN_TYPE, jwtService.getExpirationMs() / 1000);
    }

}