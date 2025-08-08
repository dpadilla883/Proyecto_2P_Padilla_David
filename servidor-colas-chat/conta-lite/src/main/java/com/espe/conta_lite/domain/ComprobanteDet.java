/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.conta_lite.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteDet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cab_id")
    private ComprobanteCab cab;

    private String cuenta;          // 6 dígitos (ej. «510100»)

    private BigDecimal debe;
    private BigDecimal haber;
}

