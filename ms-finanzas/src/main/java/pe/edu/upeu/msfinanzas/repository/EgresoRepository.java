package pe.edu.upeu.msfinanzas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msfinanzas.entity.Egreso;

import java.time.LocalDateTime;
import java.util.List;

public interface EgresoRepository extends JpaRepository<Egreso, Long> {

    List<Egreso> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
