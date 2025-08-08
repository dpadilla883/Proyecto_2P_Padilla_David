/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Fila de la matriz: un empleado y el mapa motivo → valor.
 */
public record MatrizMotivosDTO(
        Long empleadoId,
        String empleado,
        Map<String, BigDecimal> valores) { }
