package pe.edu.upeu.msauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Datos personales
    private String nombres;
    private String apellidos;
    @Column(length = 8, unique = true)
    private String dni;
    private LocalDate fechaNacimiento;

    // Datos académicos
    private String gradoActual;
    private String seccionActual;

    // Estado (activo = matriculable)
    private Boolean activo;

    // Relación con el usuario de login
    @OneToOne
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;
}
