

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author David
 */
package com.espe.nomina_api.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/** Cabecera + lista de detalles usada en POST y PUT. */
public record NominaCabReq(
        LocalDate fecha,
        String observacion,
        Long empleadoId,
        @NotEmpty List<NominaDetReq> detalles) { }
