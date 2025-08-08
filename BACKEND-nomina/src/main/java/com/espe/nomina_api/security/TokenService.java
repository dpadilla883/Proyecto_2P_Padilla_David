/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Firma y valida JWT usando la clave HMAC definida en application.yml.
 */
@Service
public class TokenService {

    @Value("${jwt.secret}")      private String secret;
    @Value("${jwt.expiration}")  private long   expirationSeconds;

    private SecretKey key;

    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Genera un JWT para un usuario OAuth2.
     *
     * @param user Principal autenticado
     * @return token firmado
     */
    public String generate(OAuth2User user) {
        Instant now   = Instant.now();
        String  roles = user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

        return Jwts.builder()
                   .setSubject(user.getName())                       // sub
                   .claim("roles", roles)                            // roles
                   .setIssuedAt(Date.from(now))                      // iat
                   .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                   .signWith(key, SignatureAlgorithm.HS256)          // firma HS256
                   .compact();
    }
}
