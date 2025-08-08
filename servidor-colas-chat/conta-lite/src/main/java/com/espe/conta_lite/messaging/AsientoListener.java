


package com.espe.conta_lite.messaging;

import com.espe.conta_lite.config.RabbitConfig;
import com.espe.conta_lite.domain.ComprobanteCab;
import com.espe.conta_lite.domain.ComprobanteDet;
import com.espe.conta_lite.repo.ComprobanteCabRepository;
import com.espe.conta_lite.repo.ComprobanteDetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsientoListener {

    private final ComprobanteCabRepository cabRepo;
    private final ComprobanteDetRepository detRepo;

    @RabbitListener(
            queues = RabbitConfig.QUEUE,
            containerFactory = "containerFactory")
    public void recibir(PayrollPostedEvent evt) {

        // ── 1. Cabecera ──────────────────────────────────────────────
        ComprobanteCab cab = ComprobanteCab.builder()
                .fecha(evt.getFecha())
                .concepto("NOM " + evt.getNominaId())
                .totalDebe(evt.getTotalDebe())
                .totalHaber(evt.getTotalHaber())
                .build();

        cabRepo.save(cab);                // ➜ guarda y genera cabId

        // ── 2. Detalles  (puede venir null) ─────────────────────────
        List<PayrollPostedEvent.Linea> lineas =
                evt.getLineas() != null ? evt.getLineas() : List.of();

        lineas.forEach(l -> detRepo.save(
                ComprobanteDet.builder()
                              .cab(cab)
                              .cuenta(l.getCuenta())
                              .debe(l.getDebe())
                              .haber(l.getHaber())
                              .build()
        ));

        log.info("📥 Asiento guardado OK  ➜ cabId={}", cab.getCabId());
    }
}

