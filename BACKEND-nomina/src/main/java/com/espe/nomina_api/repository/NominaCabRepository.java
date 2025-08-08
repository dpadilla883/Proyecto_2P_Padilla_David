/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
/* src/main/java/com/espe/nomina_api/repository/NominaCabRepository.java */
package com.espe.nomina_api.repository;

import com.espe.nomina_api.domain.NominaCab;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface NominaCabRepository extends JpaRepository<NominaCab, Long> {

    Optional<NominaCab> findTopByPeriodoOrderByNumeroDesc(String periodo);
    /* ------------- LISTADO PARA UI ------------- */
    @Query("SELECT c FROM NominaCab c JOIN FETCH c.empleado ORDER BY c.fecha DESC")

    List<NominaCab> findListado(Pageable pageable);
}
