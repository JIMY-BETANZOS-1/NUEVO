package pe.edu.upeu.mspagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.mspagos.entity.EstadoPago;
import pe.edu.upeu.mspagos.entity.Mensualidad;
import pe.edu.upeu.mspagos.entity.MesMensualidad;

import java.util.List;
import java.util.Optional;

public interface MensualidadRepository extends JpaRepository<Mensualidad, Long> {

    List<Mensualidad> findByMatriculaId(Long matriculaId);

    Optional<Mensualidad> findByMatriculaIdAndMes(Long matriculaId, MesMensualidad mes);

    List<Mensualidad> findByMatriculaIdAndEstado(Long matriculaId, EstadoPago estado);
}
