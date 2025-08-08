package com.espe.nomina_api.controller;

import com.espe.nomina_api.domain.MotivoIngresoEgreso;
import com.espe.nomina_api.service.MotivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motivos")
@SecurityRequirement(name = "oauth2")
@RequiredArgsConstructor
public class MotivoController {

    private final MotivoService service;

    @Operation(summary = "Listar motivos")
    @GetMapping
    public List<MotivoIngresoEgreso> listar() {
        return service.listar();
    }

    @Operation(summary = "Crear motivo")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MotivoIngresoEgreso crear(@Valid @RequestBody MotivoIngresoEgreso dto) {
        return service.crear(dto);
    }

    @Operation(summary = "Obtener motivo por ID")
    @GetMapping("/{id}")
    public MotivoIngresoEgreso obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @Operation(summary = "Actualizar motivo")
    @PutMapping("/{id}")
    public MotivoIngresoEgreso actualizar(@PathVariable Long id,
                                          @Valid @RequestBody MotivoIngresoEgreso dto) {
        return service.actualizar(id, dto);
    }

    @Operation(summary = "Eliminar motivo")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
