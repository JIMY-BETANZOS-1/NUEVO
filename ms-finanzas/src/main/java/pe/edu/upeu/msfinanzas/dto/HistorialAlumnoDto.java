package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialAlumnoDto {

    private String dni;
    private String nombres;
    private String apellidos;

    private List<HistorialMatriculaDto> matriculas;
}
