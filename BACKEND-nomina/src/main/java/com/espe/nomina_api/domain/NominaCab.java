/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.espe.nomina_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nomina_cab")
@Getter @Setter
public class NominaCab {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nominaId;

    /** Correlativo único: p. ej. NOM‑202507‑0003 */
    @Column(nullable = false, length = 20)
    private String numero;

       
     @Column(length = 20)
    private String estado;
    
    @NotNull private LocalDate fecha;

    /** Periodo “AAAA‑MM” derivado de la fecha; se llena automáticamente. */
    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(length = 250) private String observacion;

    /* N-1 con Empleado */
    @ManyToOne(fetch = FetchType.EAGER)          // EAGER para forzar la carga
    @JoinColumn(name = "empleado_id")
    //@JsonManagedReference("emp-nom") 
    //@JsonBackReference("emp-nom")  
    @JsonIgnoreProperties({"nominas"})
    private Empleado empleado;

    /* 1-N con detalles (esto se queda igual) */
    @OneToMany(mappedBy = "nominaCab",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("cab-det")
    private List<NominaDet> detalles = new ArrayList<>();


}
