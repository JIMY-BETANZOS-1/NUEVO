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
public class AlumnoResponseDto {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String gradoActual;
    private String seccionActual;
    private Boolean activo;

    // Info básica del usuario de login
    private Integer authUserId;
    private String userName;
}
