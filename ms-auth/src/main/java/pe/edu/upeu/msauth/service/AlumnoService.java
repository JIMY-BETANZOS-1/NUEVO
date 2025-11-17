package pe.edu.upeu.msauth.service;

import pe.edu.upeu.msauth.dto.AlumnoRequestDto;
import pe.edu.upeu.msauth.dto.AlumnoResponseDto;

import java.util.List;

public interface AlumnoService {

    AlumnoResponseDto crearAlumno(AlumnoRequestDto dto);

    AlumnoResponseDto obtenerPorId(Integer id);

    List<AlumnoResponseDto> listarTodos();

    AlumnoResponseDto buscarPorUserName(String userName);

    AlumnoResponseDto actualizarAlumno(Integer id, AlumnoRequestDto dto);

    void eliminarAlumno(Integer id);

    AlumnoResponseDto buscarPorDni(String dni);

    AlumnoResponseDto actualizarGradoSeccion(Integer id, String grado, String seccion);


}
