package pe.edu.upeu.msmatriculas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.msmatriculas.entity.EstadoMatricula;
import pe.edu.upeu.msmatriculas.entity.TipoMatricula;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaResponseDto {

    private Long id;
    private String codigoMatricula;
    private Integer anioEscolar;
    private TipoMatricula tipoMatricula;
    private String grado;
    private String seccion;
    private LocalDateTime fechaMatricula;
    private EstadoMatricula estado;

    private AlumnoResumenDto alumno;

    // 👇 NUEVO
    private ApoderadoResumenDto apoderado;
}
