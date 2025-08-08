package com.espe.nomina_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    /**
     * Devuelve:
     *  • Atributos del usuario (cuando la sesión es OAuth2);
     *  • Claims del JWT (cuando la llamada trae Authorization: Bearer …).
     */
    @GetMapping("/api/user")
    public ResponseEntity<?> currentUser(Authentication auth) {

        if (auth == null) {                         // sin credenciales
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        /* ── 1. Llamadas HECHAS justo después del login ─────────────── */
        if (auth instanceof OAuth2AuthenticationToken oauth) {
            return ResponseEntity.ok(oauth.getPrincipal().getAttributes());
        }

        /* ── 2. Llamadas con Authorization: Bearer <jwt> ───────────── */
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return ResponseEntity.ok(jwt.getClaims());          // o filtra lo que quieras
        }

        /* ── 3. Cualquier otro tipo no esperado ───────────────────── */
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
