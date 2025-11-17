package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaListadoDto {
    private Long id;
    private String codigoMatricula;
    private Integer anioEscolar;
    private String grado;
    private String seccion;
}
