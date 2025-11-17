package pe.edu.upeu.mspagos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensualidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matriculaId;

    private Integer anioEscolar;

    @Enumerated(EnumType.STRING)
    private MesMensualidad mes;

    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    private LocalDate fechaVencimiento;

    private LocalDate fechaPago;
}
