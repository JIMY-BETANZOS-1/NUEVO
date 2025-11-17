package pe.edu.upeu.mspagos.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.mspagos.dto.MatriculaFeignDto;

@FeignClient(name = "ms-matriculas", path = "/matriculas")
public interface MatriculaFeignClient {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "matriculaBuscarPorIdCB", fallbackMethod = "fallbackBuscarMatriculaPorId")
    ResponseEntity<MatriculaFeignDto> buscarMatriculaPorId(@PathVariable("id") Long id);
    // Fallback
    default ResponseEntity<MatriculaFeignDto> fallbackBuscarMatriculaPorId(Long id, Throwable e) {
        MatriculaFeignDto dto = new MatriculaFeignDto();
        dto.setId(id);
        dto.setCodigoMatricula("SERVICIO MATRICULA NO DISPONIBLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }

    @GetMapping("/codigo/{codigoMatricula}")
    @CircuitBreaker(name = "matriculaBuscarPorCodigoCB", fallbackMethod = "fallbackBuscarMatriculaPorCodigo")
    ResponseEntity<MatriculaFeignDto> buscarMatriculaPorCodigo(
            @PathVariable("codigoMatricula") String codigoMatricula
    );

    default ResponseEntity<MatriculaFeignDto> fallbackBuscarMatriculaPorCodigo(
            String codigoMatricula,
            Throwable e
    ) {
        MatriculaFeignDto dto = new MatriculaFeignDto();
        dto.setId(null);
        dto.setCodigoMatricula("SERVICIO MATRICULA NO DISPONIBLE: " + codigoMatricula);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }


}
