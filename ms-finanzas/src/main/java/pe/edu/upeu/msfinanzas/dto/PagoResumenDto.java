package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.msfinanzas.entity.EstadoPago;
import pe.edu.upeu.msfinanzas.entity.MetodoPago;
import pe.edu.upeu.msfinanzas.entity.TipoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Debe ser compatible con PagoResponseDto de ms-pagos
 * (solo los campos que necesitas).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResumenDto {

    private Long id;
    private String codigoPago;
    private Long matriculaId;
    private BigDecimal monto;
    private TipoPago tipoPago;
    private MetodoPago metodoPago;
    private EstadoPago estado;
    private String descripcion;
    private LocalDateTime fechaRegistro;
}
