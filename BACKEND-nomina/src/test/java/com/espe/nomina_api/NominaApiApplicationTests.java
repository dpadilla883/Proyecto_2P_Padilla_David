package com.espe.nomina_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")      // ← usará application-test.properties con H2
class NominaApiApplicationTests {

    @Test
    void contextLoads() {
        // si el contexto arranca sin excepciones, el test pasa
    }
}
