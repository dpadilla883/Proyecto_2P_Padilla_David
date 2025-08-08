/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.service;

import com.espe.nomina_api.dto.ValoresPagarDTO;
import com.espe.nomina_api.dto.MatrizMotivosDTO;  
import com.espe.nomina_api.repository.NominaDetRepository;
import java.math.BigDecimal;            // ← FALTABA
import java.time.LocalDate;
import java.util.ArrayList;             // ← FALTABA
import java.util.LinkedHashMap;         // ← FALTABA
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final NominaDetRepository detRepo;

    /**
     * Devuelve la suma de valores a pagar por empleado
     * entre las fechas (inclusive).
     */
    public List<ValoresPagarDTO> valoresPagar(LocalDate desde, LocalDate hasta) {
        return detRepo.findValoresPagar(desde, hasta);
    }
    
    public List<MatrizMotivosDTO> matrizMotivos(LocalDate desde, LocalDate hasta) {
        var rows = detRepo.findMatrizMotivos(desde, hasta);

        // Mapa empleadoId → DTO
        Map<Long, MatrizMotivosDTO> mapa = new LinkedHashMap<>();

        rows.forEach(r -> {
            Long   empId   = (Long)   r[0];
            String empleado= (String) r[1];
            String motivo  = (String) r[2];
            BigDecimal val = (BigDecimal) r[3];

            mapa.computeIfAbsent(empId,
                    id -> new MatrizMotivosDTO(empId, empleado, new LinkedHashMap<>()))
                .valores()
                .put(motivo, val);
        });
        return new ArrayList<>(mapa.values());
    }
}
