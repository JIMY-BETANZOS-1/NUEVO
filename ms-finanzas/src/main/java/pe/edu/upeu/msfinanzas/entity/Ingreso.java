package pe.edu.upeu.msfinanzas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pagoId;              // id del pago en ms-pagos

    private String descripcion;

    private BigDecimal monto;

    // MATRICULA, MENSUALIDAD, etc. (texto)
    private String origen;

    private LocalDateTime fecha;
}
