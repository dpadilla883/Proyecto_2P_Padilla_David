/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity @Table(name="nomina_det")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NominaDet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nominaDetId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="nomina_cab_id")
    @JsonBackReference("cab-det")
    private NominaCab nominaCab;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "motivo_id")
@NotNull
private MotivoIngresoEgreso motivo;

@DecimalMin(value = "0.01") @NotNull
private BigDecimal valor;

private String observacion;

}
