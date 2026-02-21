package com.zest.productapi.security;

import com.zest.productapi.entity.RefreshToken;
import com.zest.productapi.entity.User;
import com.zest.productapi.repo.RefreshTokenRepository;
import com.zest.productapi.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    private final long refreshTokenDuration =
            7 * 24 * 60 * 60 * 1000; // 7 days

    // ✅ CREATE OR UPDATE refresh token
    @Transactional
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Check existing token
        RefreshToken refreshToken =
                repository.findByUser(user)
                        .orElse(new RefreshToken());

        // Set values
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDuration)
        );

        return repository.save(refreshToken);
    }


    // ✅ VERIFY EXPIRY
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {

            repository.delete(token);

            throw new RuntimeException(
                    "Refresh token expired. Please login again"
            );
        }

        return token;
    }


    // ✅ FIND BY TOKEN
    public RefreshToken findByToken(String token) {

        return repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));
    }


    // ✅ DELETE (Logout support)
    @Transactional
    public void deleteByUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        repository.deleteByUser(user);
    }

}
