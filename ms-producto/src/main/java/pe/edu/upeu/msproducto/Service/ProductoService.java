package pe.edu.upeu.msproducto.Service;

import pe.edu.upeu.msproducto.Entity.Producto;
import pe.edu.upeu.msproducto.dto.ProductoDto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();

    ProductoDto buscarPorId(Integer id);

    Producto guardar(Producto producto);

    Producto actualizar(Producto producto);

    void borrarPorId(Integer id);
}
