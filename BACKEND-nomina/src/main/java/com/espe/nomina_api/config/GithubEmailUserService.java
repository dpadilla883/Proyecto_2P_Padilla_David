/*
 * Servicio que amplía la obtención de datos de usuario para GitHub.
 * Si el endpoint principal no trae e-mail, realiza una llamada extra a /user/emails
 * y garantiza que los campos del Map NO sean null (Map.of no admite valores null).
 */
package com.espe.nomina_api.config;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GithubEmailUserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        // Datos básicos que devuelve Spring para GitHub
        OAuth2User user = super.loadUser(request);

        // Si ya viene el e-mail, devolvemos el usuario sin cambios
        if (user.getAttribute("email") != null) {
            return user;
        }

        /* ----------------------------------------------------------------
         * Paso extra: GitHub solo expone el e-mail primario en /user/emails
         * ---------------------------------------------------------------- */
        String token = request.getAccessToken().getTokenValue();

        RequestEntity<Void> emailReq = RequestEntity
                .get("https://api.github.com/user/emails")
                .header("Authorization", "token " + token)
                .build();

        RestTemplate rest = new RestTemplate();
        ResponseEntity<List<Map<String, Object>>> emailResp =
                rest.exchange(emailReq, new ParameterizedTypeReference<>() {});

        String email = emailResp.getBody().stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .map(e -> (String) e.get("email"))
                .findFirst()
                .orElse("no-email@github.local");

        /* ----------------------------------------------------------------
         * GitHub a veces no envía “name”; usamos “login” o placeholder.
         * Map.of(...) lanza NPE si algún valor es null ⇒ garantizamos no-null
         * ---------------------------------------------------------------- */
        Object idObj = Objects.requireNonNull(user.getAttribute("id")); // id es obligatorio
        String id = idObj.toString();

        String name = (String) user.getAttribute("name");
        if (name == null || name.isBlank()) {
            name = user.getAttribute("login");     // fallback al username
            if (name == null) name = "unknown";    // último recurso
        }

        /* ----------------------------------------------------------------
         * Devolvemos un nuevo OAuth2User con e-mail y sin valores nulos
         * ---------------------------------------------------------------- */
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of(
                        "id",    id,
                        "name",  name,
                        "email", email
                ),
                "id"   // atributo usado como “username” por Spring Security
        );
    }
}
