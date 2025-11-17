package pe.edu.upeu.msmatriculas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msmatriculas.entity.EstadoMatricula;
import pe.edu.upeu.msmatriculas.entity.Matricula;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Long countByAnioEscolar(Integer anioEscolar);

    List<Matricula> findByAlumnoId(Integer alumnoId);

    List<Matricula> findByAnioEscolar(Integer anioEscolar);

    List<Matricula> findByAlumnoIdAndEstado(Integer alumnoId, EstadoMatricula estado);

    // 👇 NUEVO
    Optional<Matricula> findByCodigoMatricula(String codigoMatricula);

}