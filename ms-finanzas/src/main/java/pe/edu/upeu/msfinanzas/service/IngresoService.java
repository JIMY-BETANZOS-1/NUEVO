package pe.edu.upeu.msfinanzas.service;

import pe.edu.upeu.msfinanzas.dto.IngresoRequestDto;
import pe.edu.upeu.msfinanzas.dto.IngresoResponseDto;
import pe.edu.upeu.msfinanzas.dto.ResumenFinancieroDto;

import java.time.LocalDate;
import java.util.List;

public interface IngresoService {

    IngresoResponseDto registrarIngreso(IngresoRequestDto dto);

    List<IngresoResponseDto> listarIngresosRango(LocalDate fechaInicio, LocalDate fechaFin);

    ResumenFinancieroDto resumenRango(LocalDate fechaInicio, LocalDate fechaFin);
}
