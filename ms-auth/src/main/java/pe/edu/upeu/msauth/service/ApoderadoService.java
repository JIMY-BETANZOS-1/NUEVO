package pe.edu.upeu.msauth.service;

import pe.edu.upeu.msauth.dto.ApoderadoRequestDto;
import pe.edu.upeu.msauth.dto.ApoderadoResponseDto;

import java.util.List;

public interface ApoderadoService {

    ApoderadoResponseDto crearApoderado(ApoderadoRequestDto dto);

    ApoderadoResponseDto obtenerPorId(Integer id);

    List<ApoderadoResponseDto> listarTodos();

    List<ApoderadoResponseDto> listarPorAlumno(Integer alumnoId);

    ApoderadoResponseDto buscarPorUserName(String userName);

    ApoderadoResponseDto actualizarApoderado(Integer id, ApoderadoRequestDto dto);

    void eliminarApoderado(Integer id);

    ApoderadoResponseDto buscarPorDni(String dni);


}
