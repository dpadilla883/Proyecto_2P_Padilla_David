
package com.espe.conta_lite.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteCab {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabId;

    private LocalDate fecha;

    private String concepto;          // «NOM 14», «Venta 001», …

    private BigDecimal totalDebe;
    private BigDecimal totalHaber;

    @OneToMany(mappedBy = "cab", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default                       // rellena con lista vacía en el builder
    private List<ComprobanteDet> detalles = new ArrayList<>();
}


