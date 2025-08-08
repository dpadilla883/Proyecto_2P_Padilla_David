/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Catálogo maestro de motivos de ingreso o egreso
 * usado en la nómina y otros módulos.
 */
@Entity
@Table(name = "motivo_ingreso_egreso",
       uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class MotivoIngresoEgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long motivoId;

    /** Clave corta; ej.: BONO, IESS, HORAS_EXTRA */
    @Column(nullable = false, length = 30)
    @NotBlank
    private String codigo;

    /** Nombre legible que verá el usuario */
    @Column(nullable = false, length = 80)
    @NotBlank
    private String nombre;

    /** true = Ingreso (suma al neto) | false = Egreso (descuenta) */
    @Column(nullable = false)
    private boolean ingreso;
}
