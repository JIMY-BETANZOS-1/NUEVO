package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenMensualDto {
    private int mes; // 1-12
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal saldo;
}
