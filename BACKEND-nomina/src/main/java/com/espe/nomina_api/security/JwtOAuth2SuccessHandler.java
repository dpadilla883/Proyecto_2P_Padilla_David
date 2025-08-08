/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Se ejecuta al terminar exitosamente el login OAuth2 (Google / GitHub).
 * Genera el JWT y redirige al frontend: /oauth2/success?token=…
 */
@Component
public class JwtOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokens;

    public JwtOAuth2SuccessHandler(TokenService tokens) {
        this.tokens = tokens;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String jwt      = tokens.generate(user);

        // Redirigimos al frontend con el token en la query
        response.sendRedirect(
            "http://localhost:5173/oauth2/success?token=" + jwt
        );
    }
}
