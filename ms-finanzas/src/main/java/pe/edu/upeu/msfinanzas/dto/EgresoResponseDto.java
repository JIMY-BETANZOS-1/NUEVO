package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.msfinanzas.entity.CategoriaEgreso;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgresoResponseDto {

    private Long id;
    private String descripcion;
    private BigDecimal monto;
    private CategoriaEgreso categoria;
    private LocalDateTime fecha;
}
