package pe.edu.upeu.mspagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.mspagos.entity.EstadoPago;
import pe.edu.upeu.mspagos.entity.MetodoPago;
import pe.edu.upeu.mspagos.entity.TipoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDto {

    private Long id;
    private String codigoPago;
    private BigDecimal monto;
    private TipoPago tipoPago;
    private MetodoPago metodoPago;
    private EstadoPago estado;
    private String descripcion;
    private LocalDateTime fechaRegistro;

    private MatriculaResumenDto matricula;
}
