package pe.edu.upeu.msproducto.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msproducto.Entity.Producto;
import pe.edu.upeu.msproducto.Respository.ProductoRepository;
import pe.edu.upeu.msproducto.Service.ProductoService;
import pe.edu.upeu.msproducto.dto.CatagoriaDto;
import pe.edu.upeu.msproducto.dto.ProductoDto;
import pe.edu.upeu.msproducto.feign.CatalogoFeign;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CatalogoFeign catalogoFeign;

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public ProductoDto buscarPorId(Integer id) {
        Producto producto = productoRepository.findById(id).get();
        CatagoriaDto catagoriaDto = catalogoFeign.buscarPorId(producto.getIdCategoria());
        ProductoDto productoDto = new ProductoDto();
        productoDto.setId(producto.getId());
        productoDto.setNombre(producto.getNombre());
        productoDto.setDescripcion(producto.getDescripcion());
        productoDto.setCatagoria(catagoriaDto);
        return productoDto;
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void borrarPorId(Integer id) {
        productoRepository.deleteById(id);
    }
}
