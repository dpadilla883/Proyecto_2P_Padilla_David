package com.espe.nomina_api.config;

import com.espe.nomina_api.security.JwtOAuth2SuccessHandler;
import com.espe.nomina_api.config.GithubEmailUserService;      // ← paquete correcto
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    /* ---------- JWT decoder para validar los tokens entrantes ---------- */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        SecretKey key   = Keys.hmacShaKeyFor(keyBytes);
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /* ---------- Cadena de filtros principal ---------- */
    @Bean
    public SecurityFilterChain security(HttpSecurity http,
                                        GithubEmailUserService githubEmailSvc,
                                        JwtOAuth2SuccessHandler jwtSuccessHandler) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(c -> c.ignoringRequestMatchers("/logout", "/api/**", "/chat/**", "/ws/**"))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll())
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .oauth2Login(o -> o
                .successHandler(jwtSuccessHandler)              // genera y envía JWT
                .userInfoEndpoint(ui -> ui.userService(githubEmailSvc)) // solo GitHub lo necesita
            )
            .oauth2ResourceServer(o -> o.jwt())                 // consume JWT
            .logout(l -> l.logoutSuccessUrl("/"));

        return http.build();
    }
}
