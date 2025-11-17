package pe.edu.upeu.msfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialMatriculaDto {

    private Long matriculaId;
    private String codigoMatricula;
    private Integer anioEscolar;
    private String grado;
    private String seccion;

    private ResumenCuotasDto resumenCuotas;
    private List<PagoResumenDto> pagos;
}
