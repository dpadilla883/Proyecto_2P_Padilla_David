/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.espe.nomina_api.repository;

import com.espe.nomina_api.domain.NominaDet;
import com.espe.nomina_api.dto.ValoresPagarDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

//@Repository
public interface NominaDetRepository extends JpaRepository<NominaDet, Long> {

    /**  Devuelve el detalle de una cabecera (ID de la nómina). */
    List<NominaDet> findByNominaCab_NominaId(Long nominaId);
    
    
    /* ---------- NUEVO: consulta agregada para Reporte 1 ---------- */
    /* NominaDetRepository.java
   Reporte 1: Valores a pagar por empleado
--------------------------------------------------------------------*/
@Query("""
       SELECT NEW com.espe.nomina_api.dto.ValoresPagarDTO(
                 c.empleado.empleadoId,
                 CONCAT(c.empleado.apellidos,' ',c.empleado.nombres),
                 SUM(d.valor))
       FROM     NominaCab  c
       JOIN     c.detalles d
       WHERE    c.fecha BETWEEN :desde AND :hasta
       GROUP BY c.empleado.empleadoId,
                c.empleado.apellidos,
                c.empleado.nombres
       ORDER BY c.empleado.apellidos,
                c.empleado.nombres
       """)
List<ValoresPagarDTO> findValoresPagar(@Param("desde") LocalDate desde,
                                       @Param("hasta") LocalDate hasta);

    
     @Query("""
           SELECT  c.empleado.empleadoId,
                   CONCAT(c.empleado.apellidos, ' ', c.empleado.nombres),
                   m.codigo,
                   SUM(d.valor)
           FROM    NominaCab  c
           JOIN    c.detalles d
           JOIN    d.motivo  m
           WHERE   c.fecha BETWEEN :desde AND :hasta
           GROUP BY c.empleado.empleadoId,
                    c.empleado.apellidos,
                    c.empleado.nombres,
                    m.codigo
           ORDER BY c.empleado.apellidos, c.empleado.nombres, m.codigo
           """)
    List<Object[]> findMatrizMotivos(@Param("desde") LocalDate desde,
                                     @Param("hasta") LocalDate hasta);
}

