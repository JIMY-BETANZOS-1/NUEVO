package pe.edu.upeu.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta del registro combinado.
 * Devuelve el alumno y el apoderado creados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAlumnoApoderadoResponseDto {

    private AlumnoResponseDto alumno;
    private ApoderadoResponseDto apoderado;
}
