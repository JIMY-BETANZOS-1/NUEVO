package pe.edu.upeu.msauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Datos personales
    private String nombres;
    private String apellidos;

    @Column(length = 8, unique = true)
    private String dni;

    private String telefono;

    private String parentesco; // Padre, Madre, Tío, etc.

    // Apoderado principal de un alumno
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    // Relación con el usuario de login
    @OneToOne
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;
}
