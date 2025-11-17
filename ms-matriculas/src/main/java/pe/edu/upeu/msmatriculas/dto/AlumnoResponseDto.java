package pe.edu.upeu.msmatriculas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Debe tener los mismos campos que AlumnoResponseDto de ms-auth
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoResponseDto {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String gradoActual;
    private String seccionActual;
    private Boolean activo;

    private Integer authUserId;
    private String userName;
}
