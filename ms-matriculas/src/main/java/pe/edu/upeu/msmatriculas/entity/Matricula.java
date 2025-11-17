package pe.edu.upeu.msmatriculas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código tipo MAT-2025-0001
    @Column(unique = true, length = 30)
    private String codigoMatricula;

    // ID del alumno en ms-auth
    private Integer alumnoId;

    private Integer anioEscolar;

    @Enumerated(EnumType.STRING)
    private TipoMatricula tipoMatricula;

    private String grado;
    private String seccion;

    private LocalDateTime fechaMatricula;

    @Enumerated(EnumType.STRING)
    private EstadoMatricula estado;
}
