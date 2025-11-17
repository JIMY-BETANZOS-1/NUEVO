package pe.edu.upeu.msmatriculas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoResumenDto {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String parentesco;
}
