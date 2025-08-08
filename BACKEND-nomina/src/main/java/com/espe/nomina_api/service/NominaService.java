package com.espe.nomina_api.service;

import com.espe.nomina_api.domain.*;
import com.espe.nomina_api.dto.NominaCabReq;
import com.espe.nomina_api.dto.NominaDetReq;
import com.espe.nomina_api.messaging.PayrollPostedEvent;
import com.espe.nomina_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.espe.nomina_api.config.RabbitConfig.EXCHANGE;

@Service
@Transactional
@RequiredArgsConstructor
public class NominaService {

    private final NominaCabRepository           cabRepo;
    private final NominaDetRepository           detRepo;
    private final EmpleadoRepository            empRepo;
    private final MotivoIngresoEgresoRepository motRepo;
    private final RabbitTemplate                rabbit;

    /* ------------------------------------------------------------------ */
    /*  Mapeo DTO → Entidad                                               */
    /* ------------------------------------------------------------------ */
    private NominaCab toEntity(NominaCabReq dto) {
        NominaCab cab = new NominaCab();
        cab.setFecha(dto.fecha());
        cab.setObservacion(dto.observacion());
        cab.setEmpleado(empRepo.getReferenceById(dto.empleadoId()));

        dto.detalles().forEach(d -> {
            NominaDet det = new NominaDet();
            det.setMotivo(motRepo.getReferenceById(d.motivoId()));
            det.setValor(d.valor());
            det.setNominaCab(cab);
            cab.getDetalles().add(det);
        });
        return cab;
    }

    /* ------------------------------------------------------------------ */
    /*  Validaciones de la petición                                       */
    /* ------------------------------------------------------------------ */
    private void validarCabecera(NominaCabReq dto) {
        if (dto.empleadoId() == null)
            throw new IllegalArgumentException("Debe seleccionar un empleado");

        if (dto.detalles() == null || dto.detalles().isEmpty())
            throw new IllegalArgumentException("Debe añadir al menos un detalle");

        dto.detalles().forEach(d -> {
            if (d.motivoId() == null)
                throw new IllegalArgumentException("Cada detalle debe tener motivo");
            if (d.valor() == null || d.valor().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("El valor del detalle debe ser mayor que 0");
        });
    }

    /* ------------------------------------------------------------------ */
    /*  CRUD cabecera                                                     */
    /* ------------------------------------------------------------------ */
    public List<NominaCab> listar(Pageable pg) {
        return cabRepo.findListado(pg);
    }

    public NominaCab obtener(Long id) {
        return cabRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nómina no encontrada"));
    }

    public NominaCab crear(NominaCabReq dto) {
        validarCabecera(dto);
        NominaCab cab = toEntity(dto);
        validarYCompletar(cab);
        return cabRepo.save(cab);
    }

    public NominaCab actualizar(Long id, NominaCabReq dto) {
        validarCabecera(dto);
        NominaCab cab      = obtener(id);
        NominaCab cambios  = toEntity(dto);

        cab.setFecha(cambios.getFecha());
        cab.setObservacion(cambios.getObservacion());
        cab.setEmpleado(cambios.getEmpleado());

        cab.getDetalles().clear();
        cambios.getDetalles().forEach(det -> addDetalle(cab, det));

        return cab;
    }

    public void eliminar(Long id) { cabRepo.delete(obtener(id)); }

    /* ------------------------------------------------------------------ */
    /*  Helper para enlazar detalle                                       */
    /* ------------------------------------------------------------------ */
    private void addDetalle(NominaCab cab, NominaDet det) {
        det.setNominaCab(cab);
        det.setMotivo(motRepo.getReferenceById(det.getMotivo().getMotivoId()));
        cab.getDetalles().add(det);
    }

    /* ------------------------------------------------------------------ */
    /*  Asiento contable (RabbitMQ)                                       */
    /* ------------------------------------------------------------------ */
    public void publicarAsiento(Long nominaId) {
        NominaCab cab = obtener(nominaId);

        Map<String, BigDecimal> rubros = cab.getDetalles().stream()
                .collect(Collectors.groupingBy(
                        d -> d.getMotivo().getCodigo(),
                        Collectors.reducing(BigDecimal.ZERO,
                                            NominaDet::getValor, BigDecimal::add)));

        List<PayrollPostedEvent.Linea> lineas = rubros.entrySet().stream()
                .map(e -> new PayrollPostedEvent.Linea(
                        e.getKey(), e.getValue(), BigDecimal.ZERO))
                .toList();

        PayrollPostedEvent evt = new PayrollPostedEvent(
                cab.getNominaId(), cab.getFecha(),
                lineas.stream().map(PayrollPostedEvent.Linea::getDebe)
                       .reduce(BigDecimal.ZERO, BigDecimal::add),
                BigDecimal.ZERO,
                lineas);

        rabbit.convertAndSend(EXCHANGE, "payroll.posted", evt);
    }

    /* ------------------------------------------------------------------ */
    /*  Numeración y coherencia antes de guardar                          */
    /* ------------------------------------------------------------------ */
    private void validarYCompletar(NominaCab cab) {
        YearMonth ym = YearMonth.from(cab.getFecha());
        cab.setPeriodo(ym.toString());
        cab.setNumero(generarNumero(ym));

        // Asegura FK y relación bidireccional
        for (NominaDet det : cab.getDetalles()) {
            det.setNominaCab(cab);
            det.setMotivo(
                motRepo.getReferenceById(det.getMotivo().getMotivoId()));
        }
    }

    private String generarNumero(YearMonth ym) {
        String pref   = "NOM-" + ym.toString().replace("-", "");
        int   nextSeq = cabRepo.findTopByPeriodoOrderByNumeroDesc(ym.toString())
                .map(c -> Integer.parseInt(c.getNumero().substring(12)))
                .orElse(0) + 1;
        return String.format("%s-%04d", pref, nextSeq);
    }
}
