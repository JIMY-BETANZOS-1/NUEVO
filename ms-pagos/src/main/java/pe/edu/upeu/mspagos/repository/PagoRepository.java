package pe.edu.upeu.mspagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.mspagos.entity.Pago;
import pe.edu.upeu.mspagos.entity.TipoPago;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByMatriculaId(Long matriculaId);

    Optional<Pago> findFirstByMatriculaIdAndTipoPago(Long matriculaId, TipoPago tipoPago);
}
