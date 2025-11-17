package pe.edu.upeu.msfinanzas.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.msfinanzas.dto.MatriculaListadoDto;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "ms-matriculas", path = "/matriculas")
public interface MatriculaFinanzasFeignClient {

    @GetMapping("/alumno/{alumnoId}")
    @CircuitBreaker(name = "matriculasPorAlumnoCB", fallbackMethod = "fallbackMatriculasPorAlumno")
    ResponseEntity<List<MatriculaListadoDto>> listarPorAlumno(@PathVariable("alumnoId") Integer alumnoId);

    default ResponseEntity<List<MatriculaListadoDto>> fallbackMatriculasPorAlumno(
            Integer alumnoId,
            Throwable e
    ) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.emptyList());
    }
}
