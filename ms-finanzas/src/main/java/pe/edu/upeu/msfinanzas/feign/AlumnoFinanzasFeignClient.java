package pe.edu.upeu.msfinanzas.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.msfinanzas.dto.AlumnoFeignDto;

@FeignClient(name = "ms-auth", path = "/alumnos")
public interface AlumnoFinanzasFeignClient {

    @GetMapping("/by-dni/{dni}")
    @CircuitBreaker(name = "alumnoBuscarPorDniCB", fallbackMethod = "fallbackBuscarAlumnoPorDni")
    ResponseEntity<AlumnoFeignDto> buscarPorDni(@PathVariable("dni") String dni);

    default ResponseEntity<AlumnoFeignDto> fallbackBuscarAlumnoPorDni(String dni, Throwable e) {
        AlumnoFeignDto dto = new AlumnoFeignDto();
        dto.setDni(dni);
        dto.setNombres("SERVICIO AUTH NO DISPONIBLE");
        dto.setApellidos("");
        dto.setActivo(false);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }
}
