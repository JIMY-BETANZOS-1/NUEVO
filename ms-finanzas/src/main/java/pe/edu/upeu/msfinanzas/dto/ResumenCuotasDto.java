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
public class ResumenCuotasDto {

    private Long matriculaId;
    private int cuotasPagadas;
    private int cuotasPendientes;
    private BigDecimal totalPagado;
    private BigDecimal totalPendiente;
}
