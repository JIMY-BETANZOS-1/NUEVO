package pe.edu.upeu.msproducto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msproducto.Entity.Producto;
import pe.edu.upeu.msproducto.Service.ProductoService;
import pe.edu.upeu.msproducto.dto.ProductoDto;

import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    ProductoService productoService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ProductoDto buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public Producto guardar(Producto producto) {
        return productoService.guardar(producto);
    }

    @PutMapping
    public Producto actualizar(Producto producto) {
        return productoService.actualizar(producto);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.borrarPorId(id);
        return "Producto pe.edu.upeu.msproducto.Entity.Producto";
    }
}
