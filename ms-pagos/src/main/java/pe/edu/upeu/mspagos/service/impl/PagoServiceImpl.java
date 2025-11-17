package pe.edu.upeu.mspagos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.upeu.mspagos.dto.*;
import pe.edu.upeu.mspagos.entity.*;
import pe.edu.upeu.mspagos.feign.FinanzasFeignClient;
import pe.edu.upeu.mspagos.feign.MatriculaFeignClient;
import pe.edu.upeu.mspagos.repository.MensualidadRepository;
import pe.edu.upeu.mspagos.repository.PagoRepository;
import pe.edu.upeu.mspagos.service.PagoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MensualidadRepository mensualidadRepository;

    @Autowired
    private MatriculaFeignClient matriculaFeignClient;

    @Autowired
    private FinanzasFeignClient finanzasFeignClient;

    // montos base (puedes cambiarlos o leer de BD)
    private static final BigDecimal MONTO_MATRICULA = new BigDecimal("150.00");
    private static final BigDecimal MONTO_MENSUALIDAD = new BigDecimal("120.00");

    @Override
    public void generarConceptosIniciales(Long matriculaId, Integer anioEscolar) {
        // Verificar matricula en ms-matricula
        ResponseEntity<MatriculaFeignDto> resp = matriculaFeignClient.buscarMatriculaPorId(matriculaId);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null || resp.getBody().getId() == null) {
            throw new RuntimeException("No se pudo obtener la matrícula para generar pagos");
        }

        // Si ya hay mensualidades, no duplicar
        List<Mensualidad> existentes = mensualidadRepository.findByMatriculaId(matriculaId);
        if (!existentes.isEmpty()) {
            return;
        }

        // Crear pago pendiente de matrícula
        Pago pagoMatricula = Pago.builder()
                .matriculaId(matriculaId)
                .codigoPago(generarCodigoPago("MAT", matriculaId))
                .monto(MONTO_MATRICULA)
                .tipoPago(TipoPago.MATRICULA)
                .metodoPago(null)
                .estado(EstadoPago.PENDIENTE)
                .descripcion("Pago de matrícula " + anioEscolar)
                .fechaRegistro(LocalDateTime.now())
                .build();
        pagoRepository.save(pagoMatricula);

        // Crear 10 mensualidades (marzo-diciembre)
        List<MesMensualidad> meses = Arrays.asList(
                MesMensualidad.MARZO, MesMensualidad.ABRIL, MesMensualidad.MAYO,
                MesMensualidad.JUNIO, MesMensualidad.JULIO, MesMensualidad.AGOSTO,
                MesMensualidad.SETIEMBRE, MesMensualidad.OCTUBRE,
                MesMensualidad.NOVIEMBRE, MesMensualidad.DICIEMBRE
        );

        for (MesMensualidad mes : meses) {
            LocalDate venc = calcularFechaVencimiento(anioEscolar, mes);

            Mensualidad mens = Mensualidad.builder()
                    .matriculaId(matriculaId)
                    .anioEscolar(anioEscolar)
                    .mes(mes)
                    .monto(MONTO_MENSUALIDAD)
                    .estado(EstadoPago.PENDIENTE)
                    .fechaVencimiento(venc)
                    .build();
            mensualidadRepository.save(mens);
        }
    }

    @Override
    public PagoResponseDto registrarPago(PagoRequestDto request) {
        if (request.getMatriculaId() == null) {
            throw new RuntimeException("matriculaId es obligatorio");
        }
        if (request.getTipoPago() == null) {
            throw new RuntimeException("tipoPago es obligatorio");
        }
        if (request.getMetodoPago() == null) {
            throw new RuntimeException("metodoPago es obligatorio");
        }

        // Obtener datos de la matrícula para respuesta
        ResponseEntity<MatriculaFeignDto> resp = matriculaFeignClient.buscarMatriculaPorId(request.getMatriculaId());
        MatriculaFeignDto matriculaDto = resp.getBody();

        BigDecimal montoUsar = request.getMonto();

        Pago pago;

        switch (request.getTipoPago()) {
            case MATRICULA:
                pago = registrarPagoMatricula(request, montoUsar);
                break;
            case MENSUALIDAD:
                pago = registrarPagoMensualidad(request, montoUsar);
                break;
            default:
                pago = registrarPagoGenerico(request, montoUsar);
                break;
        }

        // Registrar ingreso en ms-finanzas (no rompe si falla)
        IngresoRequestDto ingresoDto = IngresoRequestDto.builder()
                .pagoId(pago.getId())
                .descripcion(pago.getDescripcion())
                .monto(pago.getMonto())
                .origen(pago.getTipoPago().name())
                .fecha(pago.getFechaRegistro())
                .build();
        finanzasFeignClient.crearIngreso(ingresoDto);

        return mapToResponse(pago, matriculaDto);
    }

    @Override
    public List<PagoResponseDto> listarPagosPorMatricula(Long matriculaId) {
        ResponseEntity<MatriculaFeignDto> resp = matriculaFeignClient.buscarMatriculaPorId(matriculaId);
        MatriculaFeignDto matDto = resp.getBody();

        return pagoRepository.findByMatriculaId(matriculaId)
                .stream()
                .map(p -> mapToResponse(p, matDto))
                .collect(Collectors.toList());
    }

    @Override
    public List<MensualidadResponseDto> listarMensualidadesPorMatricula(Long matriculaId) {
        return mensualidadRepository.findByMatriculaId(matriculaId)
                .stream()
                .map(this::mapMensualidadToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagoResponseDto obtenerPorId(Long id) {
        // 1. Buscar el pago
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        // 2. Intentar obtener la matrícula asociada
        MatriculaFeignDto matDto = null;
        if (pago.getMatriculaId() != null) {
            try {
                ResponseEntity<MatriculaFeignDto> resp = matriculaFeignClient.buscarMatriculaPorId(pago.getMatriculaId());
                if (resp.getStatusCode().is2xxSuccessful()) {
                    matDto = resp.getBody();
                }
            } catch (Exception e) {
                // si ms-matricula está caído, devolvemos solo el pago
            }
        }

        // 3. Mapear a DTO
        return mapToResponse(pago, matDto);
    }

    // ========= Helpers ==========

    private String generarCodigoPago(String prefijo, Long matriculaId) {
        long time = System.currentTimeMillis() % 100000;
        return prefijo + "-" + matriculaId + "-" + String.format("%05d", time);
    }

    private LocalDate calcularFechaVencimiento(Integer anio, MesMensualidad mes) {
        Month month;
        switch (mes) {
            case MARZO: month = Month.MARCH; break;
            case ABRIL: month = Month.APRIL; break;
            case MAYO: month = Month.MAY; break;
            case JUNIO: month = Month.JUNE; break;
            case JULIO: month = Month.JULY; break;
            case AGOSTO: month = Month.AUGUST; break;
            case SETIEMBRE: month = Month.SEPTEMBER; break;
            case OCTUBRE: month = Month.OCTOBER; break;
            case NOVIEMBRE: month = Month.NOVEMBER; break;
            case DICIEMBRE: month = Month.DECEMBER; break;
            default: month = Month.MARCH;
        }
        // Ej: 5 de cada mes
        return LocalDate.of(anio, month, 5);
    }

    private Pago registrarPagoMatricula(PagoRequestDto request, BigDecimal montoUsar) {
        Pago pendiente = pagoRepository
                .findFirstByMatriculaIdAndTipoPago(request.getMatriculaId(), TipoPago.MATRICULA)
                .orElseThrow(() -> new RuntimeException("No existe pago pendiente de matrícula"));

        if (pendiente.getEstado() == EstadoPago.PAGADO) {
            throw new RuntimeException("La matrícula ya fue pagada");
        }

        if (montoUsar == null) {
            montoUsar = pendiente.getMonto() != null ? pendiente.getMonto() : MONTO_MATRICULA;
        }

        pendiente.setMonto(montoUsar);
        pendiente.setMetodoPago(request.getMetodoPago());
        pendiente.setEstado(EstadoPago.PAGADO);
        pendiente.setDescripcion(request.getDescripcion() != null ? request.getDescripcion() : "Pago de matrícula");
        pendiente.setFechaRegistro(LocalDateTime.now());

        return pagoRepository.save(pendiente);
    }

    private Pago registrarPagoMensualidad(PagoRequestDto request, BigDecimal montoUsar) {
        if (request.getMesMensualidad() == null) {
            throw new RuntimeException("mesMensualidad es obligatorio para pagar mensualidad");
        }

        Mensualidad mensualidad = mensualidadRepository
                .findByMatriculaIdAndMes(request.getMatriculaId(), request.getMesMensualidad())
                .orElseThrow(() -> new RuntimeException("Mensualidad no encontrada para ese mes"));

        if (mensualidad.getEstado() == EstadoPago.PAGADO) {
            throw new RuntimeException("La mensualidad ya está pagada");
        }

        if (montoUsar == null) {
            montoUsar = mensualidad.getMonto() != null ? mensualidad.getMonto() : MONTO_MENSUALIDAD;
        }

        mensualidad.setEstado(EstadoPago.PAGADO);
        mensualidad.setFechaPago(LocalDate.now());
        mensualidad.setMonto(montoUsar);
        mensualidadRepository.save(mensualidad);

        Pago pago = Pago.builder()
                .matriculaId(request.getMatriculaId())
                .codigoPago(generarCodigoPago("MEN", request.getMatriculaId()))
                .monto(montoUsar)
                .tipoPago(TipoPago.MENSUALIDAD)
                .metodoPago(request.getMetodoPago())
                .estado(EstadoPago.PAGADO)
                .descripcion(request.getDescripcion() != null ?
                        request.getDescripcion() :
                        "Pago mensualidad " + request.getMesMensualidad().name())
                .fechaRegistro(LocalDateTime.now())
                .mensualidadId(mensualidad.getId())
                .build();

        return pagoRepository.save(pago);
    }

    private Pago registrarPagoGenerico(PagoRequestDto request, BigDecimal montoUsar) {
        if (montoUsar == null) {
            throw new RuntimeException("monto es obligatorio para este tipo de pago");
        }

        Pago pago = Pago.builder()
                .matriculaId(request.getMatriculaId())
                .codigoPago(generarCodigoPago("OTR", request.getMatriculaId()))
                .monto(montoUsar)
                .tipoPago(request.getTipoPago())
                .metodoPago(request.getMetodoPago())
                .estado(EstadoPago.PAGADO)
                .descripcion(request.getDescripcion())
                .fechaRegistro(LocalDateTime.now())
                .build();

        return pagoRepository.save(pago);
    }

    private PagoResponseDto mapToResponse(Pago pago, MatriculaFeignDto mat) {
        MatriculaResumenDto matRes = null;
        if (mat != null && mat.getId() != null) {
            matRes = MatriculaResumenDto.builder()
                    .id(mat.getId())
                    .codigoMatricula(mat.getCodigoMatricula())
                    .anioEscolar(mat.getAnioEscolar())
                    .grado(mat.getGrado())
                    .seccion(mat.getSeccion())
                    .build();
        }

        return PagoResponseDto.builder()
                .id(pago.getId())
                .codigoPago(pago.getCodigoPago())
                .monto(pago.getMonto())
                .tipoPago(pago.getTipoPago())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .descripcion(pago.getDescripcion())
                .fechaRegistro(pago.getFechaRegistro())
                .matricula(matRes)
                .build();
    }

    private MensualidadResponseDto mapMensualidadToDto(Mensualidad m) {
        return MensualidadResponseDto.builder()
                .id(m.getId())
                .matriculaId(m.getMatriculaId())
                .anioEscolar(m.getAnioEscolar())
                .mes(m.getMes())
                .monto(m.getMonto())
                .estado(m.getEstado())
                .fechaVencimiento(m.getFechaVencimiento())
                .fechaPago(m.getFechaPago())
                .build();
    }

    @Override
    public ResumenCuotasDto obtenerResumenCuotasPorMatricula(Long matriculaId) {
        List<Mensualidad> mensualidades = mensualidadRepository.findByMatriculaId(matriculaId);

        int pagadas = 0;
        int pendientes = 0;
        BigDecimal totalPagado = BigDecimal.ZERO;
        BigDecimal totalPendiente = BigDecimal.ZERO;

        for (Mensualidad m : mensualidades) {
            if (m.getEstado() == EstadoPago.PAGADO) {
                pagadas++;
                if (m.getMonto() != null) {
                    totalPagado = totalPagado.add(m.getMonto());
                }
            } else {
                pendientes++;
                if (m.getMonto() != null) {
                    totalPendiente = totalPendiente.add(m.getMonto());
                }
            }
        }

        return ResumenCuotasDto.builder()
                .matriculaId(matriculaId)
                .cuotasPagadas(pagadas)
                .cuotasPendientes(pendientes)
                .totalPagado(totalPagado)
                .totalPendiente(totalPendiente)
                .build();
    }

    @Override
    public ResumenCuotasDto obtenerResumenCuotasPorCodigoMatricula(String codigoMatricula) {
        // 1. Buscar la matrícula en ms-matriculas por código
        ResponseEntity<MatriculaFeignDto> resp =
                matriculaFeignClient.buscarMatriculaPorCodigo(codigoMatricula);

        MatriculaFeignDto mat = resp.getBody();

        if (mat == null || mat.getId() == null) {
            throw new RuntimeException("No se encontró matrícula para código: " + codigoMatricula);
        }

        // 2. Reusar la lógica existente por matriculaId
        return obtenerResumenCuotasPorMatricula(mat.getId());
    }

}
