/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.dto;




import java.math.BigDecimal;

/** Fila de detalle que llega en las peticiones POST/PUT. */
public record NominaDetReq(
        Long motivoId,
        BigDecimal valor) { }
