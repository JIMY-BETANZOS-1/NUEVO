// src/main/java/pe/edu/upeu/msfinanzas/service/EgresoService.java
package pe.edu.upeu.msfinanzas.service;

import pe.edu.upeu.msfinanzas.dto.EgresoRequestDto;
import pe.edu.upeu.msfinanzas.dto.EgresoResponseDto;
import pe.edu.upeu.msfinanzas.dto.ResumenCategoriaEgresoDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EgresoService {

    EgresoResponseDto registrarEgreso(EgresoRequestDto dto);

    List<EgresoResponseDto> listarEgresosRango(LocalDate fechaInicio, LocalDate fechaFin);

    // total de egresos en un rango
    BigDecimal totalEgresosRango(LocalDate fechaInicio, LocalDate fechaFin);

    // resumen por categoría (SERVICIOS, SUELDOS, etc.)
    List<ResumenCategoriaEgresoDto> resumenPorCategoria(LocalDate fechaInicio, LocalDate fechaFin);
}
