package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngresoRequestDto {

    private Long pagoId;
    private String descripcion;
    private BigDecimal monto;
    private String origen;
    private LocalDateTime fecha;
}
