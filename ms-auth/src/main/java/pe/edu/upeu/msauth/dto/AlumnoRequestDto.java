package pe.edu.upeu.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoRequestDto {

    // Datos para crear el usuario de login
    private String userName;
    private String password; // si ya existe el usuario, igual puedes enviarlo o dejarlo null

    // Datos del alumno
    private String nombres;
    private String apellidos;
    private String dni;
    private String fechaNacimiento;
    private String gradoActual;
    private String seccionActual;
}
