package pe.edu.upeu.msfinanzas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msfinanzas.entity.Ingreso;

import java.time.LocalDateTime;
import java.util.List;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    List<Ingreso> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
