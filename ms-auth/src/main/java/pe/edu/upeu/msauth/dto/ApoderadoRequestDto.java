package pe.edu.upeu.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoRequestDto {

    // Datos para crear el usuario de login
    private String userName;
    private String password;

    // Datos del apoderado
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String parentesco;

    // Alumno al que representa
    private Integer alumnoId;
}
