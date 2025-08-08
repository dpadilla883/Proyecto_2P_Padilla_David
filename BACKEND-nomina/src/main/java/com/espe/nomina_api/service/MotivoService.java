package com.espe.nomina_api.service;

import com.espe.nomina_api.domain.MotivoIngresoEgreso;
import com.espe.nomina_api.repository.MotivoIngresoEgresoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MotivoService {

    private final MotivoIngresoEgresoRepository repo;

    /* ──────── CRUD ──────── */

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<MotivoIngresoEgreso> listar() {
        return repo.findAll();
    }

    public MotivoIngresoEgreso crear(MotivoIngresoEgreso dto) {
        validarCodigoUnico(dto.getCodigo(), null);
        dto.setCodigo(dto.getCodigo().trim().toUpperCase());
        return repo.save(dto);
    }

    public MotivoIngresoEgreso obtener(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Motivo " + id + " no encontrado"));
    }

    public MotivoIngresoEgreso actualizar(Long id, MotivoIngresoEgreso dto) {
        validarCodigoUnico(dto.getCodigo(), id);
        MotivoIngresoEgreso m = obtener(id);
        m.setCodigo(dto.getCodigo().trim().toUpperCase());
        m.setNombre(dto.getNombre());
        m.setIngreso(dto.isIngreso());
        return repo.save(m);
    }

    // MotivoService.java
@Transactional
public void eliminar(Long id) {
    try {
        repo.deleteById(id);          // intenta borrar
    } catch (DataIntegrityViolationException ex) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El motivo está siendo utilizado en alguna nómina y no puede eliminarse",
                ex);
    }
}


    /* ──────── Helpers ──────── */

    private void validarCodigoUnico(String codigo, Long idActual) {
        if (repo.existsByCodigoIgnoreCase(codigo) &&
            (idActual == null ||             // creación
             repo.findById(idActual)
                 .filter(m -> !m.getCodigo().equalsIgnoreCase(codigo))
                 .isPresent())) {            // edición con cambio duplicado
            throw new IllegalArgumentException("El código ya existe: " + codigo);
        }
    }
}
