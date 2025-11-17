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
public class IngresoResponseDto {

    private Long id;
    private Long pagoId;
    private String descripcion;
    private BigDecimal monto;
    private String origen;
    private LocalDateTime fecha;

    // info opcional del pago en ms-pagos
    private PagoResumenDto pago;
}
