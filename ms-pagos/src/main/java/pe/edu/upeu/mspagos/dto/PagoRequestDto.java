package pe.edu.upeu.mspagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.mspagos.entity.MesMensualidad;
import pe.edu.upeu.mspagos.entity.MetodoPago;
import pe.edu.upeu.mspagos.entity.TipoPago;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequestDto {

    private Long matriculaId;

    private TipoPago tipoPago;

    // obligatorio si es mensualidad
    private MesMensualidad mesMensualidad;

    private BigDecimal monto;

    private MetodoPago metodoPago;

    private String descripcion;
}
