package pe.edu.upeu.mspagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.mspagos.entity.EstadoPago; // no usar aquí, solo simple
import java.time.LocalDateTime;

/**
 * Debe coincidir con MatriculaResponseDto de ms-matricula
 * (solo los campos que necesitarás).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaFeignDto {

    private Long id;
    private String codigoMatricula;
    private Integer anioEscolar;
    private String grado;
    private String seccion;
    private LocalDateTime fechaMatricula;
    private String estado; // texto
}
