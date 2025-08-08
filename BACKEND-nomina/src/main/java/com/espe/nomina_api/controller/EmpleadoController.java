/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.controller;

import com.espe.nomina_api.domain.Empleado;
import com.espe.nomina_api.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService service;

    @Operation(summary = "Listar empleados")
    @GetMapping
    public List<Empleado> listar() { return service.listar(); }

    @Operation(summary = "Crear empleado")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado crear(@Valid @RequestBody Empleado e) { return service.crear(e); }

    @Operation(summary = "Obtener empleado")
    @GetMapping("/{id}")
    public Empleado obtener(@PathVariable Long id) { return service.obtener(id); }

    @Operation(summary = "Actualizar empleado")
    @PutMapping("/{id}")
    public Empleado actualizar(@PathVariable Long id,
                               @Valid @RequestBody Empleado dto) {
        return service.actualizar(id, dto);
    }

    @Operation(summary = "Eliminar empleado")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
