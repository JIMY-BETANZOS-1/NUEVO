package pe.edu.upeu.msauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msauth.entity.Alumno;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    Optional<Alumno> findByDni(String dni);

    Optional<Alumno> findByAuthUser_UserName(String userName);
}
