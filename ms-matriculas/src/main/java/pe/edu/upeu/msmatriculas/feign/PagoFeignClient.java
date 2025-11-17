package pe.edu.upeu.msmatriculas.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-pagos", path = "/pagos")
public interface PagoFeignClient {

    @PostMapping("/inicial/{matriculaId}")
    @CircuitBreaker(name = "pagosGenerarConceptosInicialesCB", fallbackMethod = "fallbackGenerarConceptosIniciales")
    ResponseEntity<Void> generarConceptosIniciales(
            @PathVariable("matriculaId") Long matriculaId,
            @RequestParam Integer anioEscolar
    );

    // Fallback cuando ms-pagos está caído o falla
    default ResponseEntity<Void> fallbackGenerarConceptosIniciales(
            Long matriculaId,
            Integer anioEscolar,
            Throwable e
    ) {
        // sólo devolvemos 503, no rompemos la matrícula
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
