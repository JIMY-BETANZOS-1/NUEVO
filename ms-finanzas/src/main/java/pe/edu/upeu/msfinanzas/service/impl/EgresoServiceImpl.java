// src/main/java/pe/edu/upeu/msfinanzas/service/impl/EgresoServiceImpl.java
package pe.edu.upeu.msfinanzas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msfinanzas.dto.EgresoRequestDto;
import pe.edu.upeu.msfinanzas.dto.EgresoResponseDto;
import pe.edu.upeu.msfinanzas.dto.ResumenCategoriaEgresoDto;
import pe.edu.upeu.msfinanzas.entity.CategoriaEgreso;
import pe.edu.upeu.msfinanzas.entity.Egreso;
import pe.edu.upeu.msfinanzas.repository.EgresoRepository;
import pe.edu.upeu.msfinanzas.service.EgresoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EgresoServiceImpl implements EgresoService {

    @Autowired
    private EgresoRepository egresoRepository;

    @Override
    public EgresoResponseDto registrarEgreso(EgresoRequestDto dto) {
        LocalDateTime fecha = dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now();

        Egreso egreso = Egreso.builder()
                .descripcion(dto.getDescripcion())
                .monto(dto.getMonto())
                .categoria(dto.getCategoria())
                .fecha(fecha)
                .build();

        Egreso guardado = egresoRepository.save(egreso);
        return mapToResponse(guardado);
    }

    @Override
    public List<EgresoResponseDto> listarEgresosRango(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        return egresoRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // total egresos
    @Override
    public BigDecimal totalEgresosRango(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        return egresoRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(Egreso::getMonto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // resumen por categoría
    @Override
    public List<ResumenCategoriaEgresoDto> resumenPorCategoria(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<Egreso> egresos = egresoRepository.findByFechaBetween(inicio, fin);

        Map<CategoriaEgreso, BigDecimal> mapa = new EnumMap<>(CategoriaEgreso.class);

        for (Egreso e : egresos) {
            if (e.getCategoria() == null || e.getMonto() == null) continue;
            mapa.merge(e.getCategoria(), e.getMonto(), BigDecimal::add);
        }

        return mapa.entrySet().stream()
                .map(entry -> ResumenCategoriaEgresoDto.builder()
                        .categoria(entry.getKey().name())
                        .total(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private EgresoResponseDto mapToResponse(Egreso e) {
        return EgresoResponseDto.builder()
                .id(e.getId())
                .descripcion(e.getDescripcion())
                .monto(e.getMonto())
                .categoria(e.getCategoria())
                .fecha(e.getFecha())
                .build();
    }
}
