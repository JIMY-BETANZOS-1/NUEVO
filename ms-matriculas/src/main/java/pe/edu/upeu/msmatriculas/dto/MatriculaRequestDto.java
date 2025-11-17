package pe.edu.upeu.msmatriculas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.msmatriculas.entity.TipoMatricula;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRequestDto {

    // usuario logeado (alumno)
    private String userName;       // viene del login

    private Integer anioEscolar;
    private TipoMatricula tipoMatricula;
    private String grado;
    private String seccion;
}