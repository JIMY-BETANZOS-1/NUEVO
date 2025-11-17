package pe.edu.upeu.mspagos.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.mspagos.dto.IngresoRequestDto;

@FeignClient(name = "ms-finanzas", path = "/ingresos")
public interface FinanzasFeignClient {

    @PostMapping
    @CircuitBreaker(name = "ingresoCrearCB", fallbackMethod = "fallbackCrearIngreso")
    ResponseEntity<Void> crearIngreso(@RequestBody IngresoRequestDto dto);

    default ResponseEntity<Void> fallbackCrearIngreso(IngresoRequestDto dto, Throwable e) {
        // solo retornamos 503, sin romper el flujo de pago
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
