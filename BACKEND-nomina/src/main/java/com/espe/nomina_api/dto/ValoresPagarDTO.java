/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.dto;

import java.math.BigDecimal;

/** Proyección sólo‑lectura para Reporte 1. */
public record ValoresPagarDTO(
        Long empleadoId,
        String empleado,
        BigDecimal valorARecibir) { }
