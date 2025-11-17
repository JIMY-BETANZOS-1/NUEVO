package pe.edu.upeu.msmatriculas.service;

import pe.edu.upeu.msmatriculas.dto.MatriculaRequestDto;
import pe.edu.upeu.msmatriculas.dto.MatriculaResponseDto;
import pe.edu.upeu.msmatriculas.entity.EstadoMatricula;

import java.util.List;

public interface MatriculaService {

    MatriculaResponseDto crearMatricula(MatriculaRequestDto request);

    MatriculaResponseDto obtenerPorId(Long id);

    List<MatriculaResponseDto> listarTodas();

    List<MatriculaResponseDto> listarPorAlumno(Integer alumnoId);

    List<MatriculaResponseDto> listarPorAnio(Integer anioEscolar);

    MatriculaResponseDto cambiarEstado(Long id, EstadoMatricula nuevoEstado);

    MatriculaResponseDto obtenerPorCodigo(String codigoMatricula);

    List<MatriculaResponseDto> listarPorDniAlumno(String dni);

    List<MatriculaResponseDto> listarPorDniApoderado(String dni);

}