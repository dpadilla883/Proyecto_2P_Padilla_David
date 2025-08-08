/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.messaging;



import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollPostedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long          nominaId;
    private LocalDate     fecha;
    private BigDecimal    totalDebe;
    private BigDecimal    totalHaber;
    private List<Linea>   lineas = new ArrayList<>();   // ← nunca null

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Linea implements Serializable {
        private static final long serialVersionUID = 1L;

        private String      cuenta;
        private BigDecimal  debe;
        private BigDecimal  haber;
    }
}
