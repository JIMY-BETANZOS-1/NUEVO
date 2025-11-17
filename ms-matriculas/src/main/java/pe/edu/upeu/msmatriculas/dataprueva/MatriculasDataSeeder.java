package pe.edu.upeu.msmatriculas.dataprueva;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msmatriculas.entity.EstadoMatricula;
import pe.edu.upeu.msmatriculas.entity.Matricula;
import pe.edu.upeu.msmatriculas.entity.TipoMatricula;
import pe.edu.upeu.msmatriculas.repository.MatriculaRepository;

import java.time.LocalDateTime;

@Component
public class MatriculasDataSeeder implements CommandLineRunner {

    private final MatriculaRepository matriculaRepository;

    public MatriculasDataSeeder(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Si ya hay datos, no volvemos a insertar
        if (matriculaRepository.count() > 0) {
            return;
        }

        // ⭐ Alumno 1: tendrá TODO pagado en 2024
        Matricula m1 = Matricula.builder()
                .codigoMatricula("MAT-2024-0001")
                .alumnoId(1) // coincide con alumno en ms-auth (ejemplo)
                .anioEscolar(2024)
                .tipoMatricula(TipoMatricula.CONTINUIDAD)
                .grado("5°")
                .seccion("A")
                .fechaMatricula(LocalDateTime.of(2024, 2, 10, 9, 0))
                .estado(EstadoMatricula.MATRICULADO)
                .build();
        m1 = matriculaRepository.save(m1); // ID esperado: 1

        // Alumno 2: matrícula 2024, algunos pagos pendientes
        Matricula m2 = Matricula.builder()
                .codigoMatricula("MAT-2024-0002")
                .alumnoId(2)
                .anioEscolar(2024)
                .tipoMatricula(TipoMatricula.INGRESO_NUEVO)
                .grado("4°")
                .seccion("B")
                .fechaMatricula(LocalDateTime.of(2024, 2, 12, 10, 30))
                .estado(EstadoMatricula.MATRICULADO)
                .build();
        m2 = matriculaRepository.save(m2); // ID esperado: 2

        // Alumno 3: matrícula 2025
        Matricula m3 = Matricula.builder()
                .codigoMatricula("MAT-2025-0001")
                .alumnoId(3)
                .anioEscolar(2025)
                .tipoMatricula(TipoMatricula.TRASLADO)
                .grado("3°")
                .seccion("C")
                .fechaMatricula(LocalDateTime.of(2025, 1, 5, 11, 0))
                .estado(EstadoMatricula.MATRICULADO)
                .build();
        m3 = matriculaRepository.save(m3); // ID esperado: 3

        // Comentario: asumimos que las IDs generadas serán 1, 2 y 3,
        // y esas mismas se usarán como referencia en ms-pagos y ms-finanzas.
    }
}
