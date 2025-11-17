package pe.edu.upeu.msfinanzas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msfinanzas.dto.IngresoRequestDto;
import pe.edu.upeu.msfinanzas.dto.IngresoResponseDto;
import pe.edu.upeu.msfinanzas.dto.PagoResumenDto;
import pe.edu.upeu.msfinanzas.dto.ResumenFinancieroDto;
import pe.edu.upeu.msfinanzas.entity.Ingreso;
import pe.edu.upeu.msfinanzas.feign.PagoFeignClient;
import pe.edu.upeu.msfinanzas.repository.IngresoRepository;
import pe.edu.upeu.msfinanzas.service.IngresoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngresoServiceImpl implements IngresoService {

    @Autowired
    private IngresoRepository ingresoRepository;

    @Autowired
    private PagoFeignClient pagoFeignClient;

    @Override
    public IngresoResponseDto registrarIngreso(IngresoRequestDto dto) {
        // si fecha viene null, usamos ahora
        LocalDateTime fecha = dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now();

        Ingreso ingreso = Ingreso.builder()
                .pagoId(dto.getPagoId())
                .descripcion(dto.getDescripcion())
                .monto(dto.getMonto())
                .origen(dto.getOrigen())
                .fecha(fecha)
                .build();

        Ingreso guardado = ingresoRepository.save(ingreso);

        PagoResumenDto pagoDto = obtenerPago(guardado.getPagoId());

        return mapToResponse(guardado, pagoDto);
    }

    @Override
    public List<IngresoResponseDto> listarIngresosRango(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<Ingreso> ingresos = ingresoRepository.findByFechaBetween(inicio, fin);

        return ingresos.stream()
                .map(i -> mapToResponse(i, obtenerPago(i.getPagoId())))
                .collect(Collectors.toList());
    }

    @Override
    public ResumenFinancieroDto resumenRango(LocalDate fechaInicio, LocalDate fechaFin) {
        List<IngresoResponseDto> ingresos = listarIngresosRango(fechaInicio, fechaFin);

        BigDecimal totalIngresos = ingresos.stream()
                .map(IngresoResponseDto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Los egresos los suma EgresoService; aquí solo devolvemos ingresos,
        // y en el controlador combinamos si quieres. Para simplificar:
        return ResumenFinancieroDto.builder()
                .totalIngresos(totalIngresos)
                .totalEgresos(BigDecimal.ZERO)
                .saldo(totalIngresos)
                .build();
    }

    // ===== helpers =====

    private PagoResumenDto obtenerPago(Long pagoId) {
        if (pagoId == null) return null;

        try {
            ResponseEntity<PagoResumenDto> resp = pagoFeignClient.buscarPagoPorId(pagoId);
            if (resp.getStatusCode().is2xxSuccessful()) {
                return resp.getBody();
            }
        } catch (Exception e) {
            // ignoramos y devolvemos null
        }
        return null;
    }

    private IngresoResponseDto mapToResponse(Ingreso i, PagoResumenDto pago) {
        return IngresoResponseDto.builder()
                .id(i.getId())
                .pagoId(i.getPagoId())
                .descripcion(i.getDescripcion())
                .monto(i.getMonto())
                .origen(i.getOrigen())
                .fecha(i.getFecha())
                .pago(pago)
                .build();
    }
}
