package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoFeignDto {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String gradoActual;
    private String seccionActual;
    private Boolean activo;
}
