/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.conta_lite.messaging;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollPostedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long nominaId;
    private LocalDate fecha;
    private BigDecimal totalDebe;
    private BigDecimal totalHaber;
    private List<Linea> lineas;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Linea implements Serializable {
        private static final long serialVersionUID = 1L;

        private String cuenta;
        private BigDecimal debe;
        private BigDecimal haber;
    }
}
