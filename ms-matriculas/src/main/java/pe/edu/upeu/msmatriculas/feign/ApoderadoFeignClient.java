package pe.edu.upeu.msmatriculas.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msmatriculas.dto.ApoderadoResponseDto;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "ms-auth", path = "/apoderados", contextId = "apoderadoFeignClient")
public interface ApoderadoFeignClient {

    // Buscar por DNI (ya lo estabas usando)
    @GetMapping("/by-dni/{dni}")
    @CircuitBreaker(name = "apoderadoPorDniCB", fallbackMethod = "fallbackApoderadoPorDni")
    ResponseEntity<ApoderadoResponseDto> buscarApoderadoPorDni(@PathVariable("dni") String dni);

    // 👇 NUEVO: listar apoderados de un alumno
    @GetMapping("/alumno/{alumnoId}")
    @CircuitBreaker(name = "apoderadoPorAlumnoCB", fallbackMethod = "fallbackApoderadoPorAlumno")
    ResponseEntity<List<ApoderadoResponseDto>> listarPorAlumno(@PathVariable("alumnoId") Integer alumnoId);

    // ===== fallbacks =====
    default ResponseEntity<ApoderadoResponseDto> fallbackApoderadoPorDni(String dni, Throwable e) {
        return ResponseEntity.ok(null);
    }

    default ResponseEntity<List<ApoderadoResponseDto>> fallbackApoderadoPorAlumno(Integer alumnoId, Throwable e) {
        return ResponseEntity.ok(Collections.emptyList());
    }
}
