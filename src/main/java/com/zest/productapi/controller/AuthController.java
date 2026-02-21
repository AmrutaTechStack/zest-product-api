package com.zest.productapi.controller;

import com.zest.productapi.dto.AuthResponse;
import com.zest.productapi.dto.LoginRequest;
import com.zest.productapi.dto.RefreshTokenRequest;
import com.zest.productapi.entity.RefreshToken;
import com.zest.productapi.security.JwtTokenProvider;
import com.zest.productapi.security.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String accessToken =
                jwtTokenProvider.generateToken(userDetails);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(
                        userDetails.getUsername()
                );

        AuthResponse response = new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );

        return ResponseEntity.ok(response);
    }

    // ================= REFRESH TOKEN =================

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.verifyExpiration(
                        refreshTokenService.findByToken(
                                request.getRefreshToken()
                        )
                );

        String username = refreshToken.getUser().getUsername();
        String role = refreshToken.getUser().getRole().name();

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        username,
                        refreshToken.getUser().getPassword(),
                        List.of(new SimpleGrantedAuthority(role))
                );

        String newAccessToken =
                jwtTokenProvider.generateToken(userDetails);

        AuthResponse response = new AuthResponse(
                newAccessToken,
                refreshToken.getToken(),
                "Bearer"
        );

        return ResponseEntity.ok(response);
    }
}
