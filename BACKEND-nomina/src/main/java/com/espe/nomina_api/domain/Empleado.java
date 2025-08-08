/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonIgnore;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo maestro de empleados.
 */
@Entity
@Table(name = "empleado")
@Getter @Setter
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empleadoId;

    /** Número de cédula ecuatoriana, 10 dígitos, único. */
    @Column(nullable = false, length = 10)
    @Pattern(regexp = "\\d{10}", message = "La cédula debe tener 10 dígitos numéricos")
    private String cedula;

    @Column(nullable = false, length = 60)
    @NotBlank
    private String nombres;

    @Column(nullable = false, length = 60)
    @NotBlank
    private String apellidos;

    /** Fecha de ingreso: no puede ser futura. */
    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;

    /** Sueldo base mensual. */
    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "El sueldo debe ser mayor que 0")
    private BigDecimal sueldoBase;

    /* Relación 1-N con nóminas */
    @OneToMany(mappedBy = "empleado",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    //@JsonBackReference("emp-nom")
    //@JsonIgnoreProperties("emp-nom") 
    //@JsonManagedReference("emp-nom") 
    @JsonIgnore
    private List<NominaCab> nominas = new ArrayList<>();



}
