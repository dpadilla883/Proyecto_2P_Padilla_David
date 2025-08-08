package com.espe.nomina_api.controller;

import com.espe.nomina_api.domain.NominaCab;
import com.espe.nomina_api.dto.NominaCabReq;
import com.espe.nomina_api.service.NominaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/nominas")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class NominaController {

    private final NominaService service;

    /* ---------- LISTAR / OBTENER ---------- */
    @GetMapping
    public List<NominaCab> listar(Pageable pg) { return service.listar(pg); }

    @GetMapping("/{id}")
    public NominaCab obtener(@PathVariable long id) { return service.obtener(id); }

    /* ---------- CREAR ---------- */
    @Operation(summary = "Crear nómina (cabecera + detalles)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long crear(@Valid @RequestBody NominaCabReq dto) {      // ← devuelve solo el ID
        return service.crear(dto).getNominaId();
    }

    /* ---------- ACTUALIZAR ---------- */
    @Operation(summary = "Actualizar nómina")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)                         // ← 204 sin cuerpo
    public void actualizar(@PathVariable Long id,
                           @Valid @RequestBody NominaCabReq dto) {
        service.actualizar(id, dto);
    }

    /* ---------- ELIMINAR ---------- */
    @Operation(summary = "Eliminar nómina")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    /* ---------- ASIENTO CONTABLE ---------- */
    @Operation(summary = "Publicar asiento contable en RabbitMQ")
    @PostMapping("/{id}/asiento")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publicarAsiento(@PathVariable Long id) { service.publicarAsiento(id); }
}
