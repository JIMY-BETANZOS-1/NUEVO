package pe.edu.upeu.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoResponseDto {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String parentesco;

    private Integer alumnoId;
    private String alumnoNombreCompleto;

    // Info del login
    private Integer authUserId;
    private String userName;
}
