package pe.edu.upeu.msproducto.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msproducto.Entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
