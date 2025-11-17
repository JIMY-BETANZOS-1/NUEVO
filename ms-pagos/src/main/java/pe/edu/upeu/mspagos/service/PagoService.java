package pe.edu.upeu.mspagos.service;

import pe.edu.upeu.mspagos.dto.MensualidadResponseDto;
import pe.edu.upeu.mspagos.dto.PagoRequestDto;
import pe.edu.upeu.mspagos.dto.PagoResponseDto;
import pe.edu.upeu.mspagos.dto.ResumenCuotasDto;

import java.util.List;

public interface PagoService {

    // generar conceptos iniciales (matrícula + 10 mensualidades)
    void generarConceptosIniciales(Long matriculaId, Integer anioEscolar);

    // registrar un pago (matrícula o mensualidad)
    PagoResponseDto registrarPago(PagoRequestDto request);

    List<PagoResponseDto> listarPagosPorMatricula(Long matriculaId);

    List<MensualidadResponseDto> listarMensualidadesPorMatricula(Long matriculaId);

    PagoResponseDto obtenerPorId(Long id);

    ResumenCuotasDto obtenerResumenCuotasPorMatricula(Long matriculaId);

    ResumenCuotasDto obtenerResumenCuotasPorCodigoMatricula(String codigoMatricula);

}
