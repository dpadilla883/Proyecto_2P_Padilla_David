/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.controller;

import com.espe.nomina_api.dto.ValoresPagarDTO;
import com.espe.nomina_api.dto.MatrizMotivosDTO;
import com.espe.nomina_api.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService service;

    /** Reporte 1: Valores a pagar por empleado. */
    @Operation(summary = "Valores a pagar (por empleado) en un rango de fechas")
    @GetMapping("/valores-a-pagar")
    public List<ValoresPagarDTO> valoresPagar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return service.valoresPagar(desde, hasta);
    }
    
    /** Reporte 2: Matriz Motivos × Empleados */
    @Operation(summary = "Matriz Motivos × Empleados en rango de fechas")
    @GetMapping("/matriz-motivos")
    public List<MatrizMotivosDTO> matrizMotivos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return service.matrizMotivos(desde, hasta);
    }
}
