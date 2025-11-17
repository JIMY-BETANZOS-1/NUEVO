package pe.edu.upeu.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para registrar Alumno + Apoderado en una sola llamada.
 * Reusa AlumnoRequestDto y ApoderadoRequestDto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAlumnoApoderadoRequestDto {

    private AlumnoRequestDto alumno;
    private ApoderadoRequestDto apoderado;
}
