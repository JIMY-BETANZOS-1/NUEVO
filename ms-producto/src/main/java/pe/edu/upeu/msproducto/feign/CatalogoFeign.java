package pe.edu.upeu.msproducto.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.msproducto.dto.CatagoriaDto;

@FeignClient(name = "ms-catalogo", path = "/categoria")
public interface CatalogoFeign {
    @GetMapping("/{id}")
    @CircuitBreaker(name = "categoriaListarPorIdCB", fallbackMethod = "fallbackCategoria")
    public CatagoriaDto buscarPorId(@PathVariable Integer id);

    default CatagoriaDto fallbackCategoria(Integer id, Exception e) {
        CatagoriaDto catagoriaDto = new CatagoriaDto();
        catagoriaDto.setId(9000000);
        catagoriaDto.setNombre("Servicio Categoria no disponible");
        return catagoriaDto;
    }

}
