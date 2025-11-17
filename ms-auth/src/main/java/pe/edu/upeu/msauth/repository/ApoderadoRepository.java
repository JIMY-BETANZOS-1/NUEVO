package pe.edu.upeu.msauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msauth.entity.Apoderado;

import java.util.List;
import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<Apoderado, Integer> {

    Optional<Apoderado> findByDni(String dni);

    Optional<Apoderado> findByAuthUser_UserName(String userName);

    List<Apoderado> findByAlumno_Id(Integer alumnoId);
}
