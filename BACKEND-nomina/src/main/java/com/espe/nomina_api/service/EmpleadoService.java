/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.service;

import com.espe.nomina_api.domain.Empleado;
import com.espe.nomina_api.repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;

    /* ----------------  CRUD  ---------------- */

    public List<Empleado> listar() {
        return repo.findAll(Sort.by("apellidos", "nombres"));
    }

    public Empleado crear(Empleado e) {
        //validarCedula(e.getCedula(), null);
        //normalizarNombre(e);
        return repo.save(e);
    }

    public Empleado obtener(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
    }

    public Empleado actualizar(Long id, Empleado cambios) {
        //validarCedula(cambios.getCedula(), id);
        Empleado e = obtener(id);

        e.setCedula(cambios.getCedula());
        e.setNombres(cambios.getNombres());
        e.setApellidos(cambios.getApellidos());
        e.setFechaIngreso(cambios.getFechaIngreso());
        e.setSueldoBase(cambios.getSueldoBase());

        //normalizarNombre(e);
        return repo.save(e);
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Empleado no existe");
        }
        repo.deleteById(id);
    }

    
    /** ------------- helpers ------------- */
/*
    //Valida unicidad y checksum de la cédula ecuatoriana. 
    private void validarCedula(String cedula, Long idActual) {
        if (!cedula.matches("\\d{10}") || !cedulaValidaEc(cedula)) {
            throw new IllegalArgumentException("Cédula ecuatoriana inválida");
        }
        if (repo.existsByCedula(cedula) &&
            repo.findByCedula(cedula)
                .filter(e -> !e.getEmpleadoId().equals(idActual))
                .isPresent()) {
            throw new IllegalStateException("La cédula ya está registrada");
        }
    }

    // Algoritmo oficial de verificación (módulo 10). 
    private boolean cedulaValidaEc(String cedula) {
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) return false;

        int tercerDig = Character.getNumericValue(cedula.charAt(2));
        if (tercerDig >= 6) return false;

        int sumaPar = 0, sumaImpar = 0;
        for (int i = 0; i < 9; i++) {
            int dig = Character.getNumericValue(cedula.charAt(i));
            if (i % 2 == 0) { // posiciones impares (0‑index)
                int mult = dig * 2;
                if (mult > 9) mult -= 9;
                sumaImpar += mult;
            } else {
                sumaPar += dig;
            }
        }
        int total = sumaPar + sumaImpar;
        int decenaSup = (total + 9) / 10 * 10;
        int verificador = decenaSup - total;
        if (verificador == 10) verificador = 0;
        return verificador == Character.getNumericValue(cedula.charAt(9));
    }

    // Normaliza nombres propios a “Título” y apellidos en mayúsculas. 
    private void normalizarNombre(Empleado e) {
        e.setNombres(formatear(e.getNombres(), true));
        e.setApellidos(formatear(e.getApellidos(), false));
    }

    private String formatear(String texto, boolean capitalizar) {
        texto = texto.trim().replaceAll("\\s{2,}", " ");
        return capitalizar
               ? texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase()
               : texto.toUpperCase();
    }
    */
}
