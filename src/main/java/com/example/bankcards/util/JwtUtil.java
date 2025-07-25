package com.example.bankcards.util;

import com.example.bankcards.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.CollectionUtils;


@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;

    /**
     * Создает JWT-токен для идентификации карты при передаче его из сервиса.
     *
     * @param cardNumber Зашифрованный номер карты.
     * @return JWT-токен.
     */
    public String generateCardToken(String cardNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("card_number", cardNumber);
        return buildToken(claims);
    }

    /**
     * Создает JWT-токен для работы пользователя с API.
     * @param userDetails Данные о пользователе.
     * @return JWT-токен.
     */
    public String generateUserToken(UserDetails userDetails) {
        if (Objects.isNull(userDetails)) {
            throw new BadCredentialsException("UserDetails is null.");
        }
        if (CollectionUtils.isEmpty(userDetails.getAuthorities())) {
            throw new BadCredentialsException("User hasn't any role");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().stream().findFirst().get());
        claims.put("username", userDetails.getUsername());
        return buildToken(claims);
    }

    protected String buildToken(Map<String, Object> claims) {
        var now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(jwtConfig.getLifetime())))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .compact();
    }
}
