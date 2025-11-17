package pe.edu.upeu.mspagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.mspagos.entity.EstadoPago;
import pe.edu.upeu.mspagos.entity.MesMensualidad;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensualidadResponseDto {

    private Long id;
    private Long matriculaId;
    private Integer anioEscolar;
    private MesMensualidad mes;
    private BigDecimal monto;
    private EstadoPago estado;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
}
