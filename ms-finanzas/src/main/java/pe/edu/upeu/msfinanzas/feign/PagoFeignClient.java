package pe.edu.upeu.msfinanzas.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msfinanzas.dto.PagoResumenDto;
import pe.edu.upeu.msfinanzas.dto.ResumenCuotasDto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@FeignClient(name = "ms-pagos", path = "/pagos")
public interface PagoFeignClient {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "pagoBuscarPorIdCB", fallbackMethod = "fallbackBuscarPagoPorId")
    ResponseEntity<PagoResumenDto> buscarPagoPorId(@PathVariable("id") Long id);

    default ResponseEntity<PagoResumenDto> fallbackBuscarPagoPorId(Long id, Throwable e) {
        PagoResumenDto dto = new PagoResumenDto();
        dto.setId(id);
        dto.setCodigoPago("SERVICIO PAGOS NO DISPONIBLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }

    @GetMapping("/matricula/{matriculaId}")
    @CircuitBreaker(name = "pagosPorMatriculaCB", fallbackMethod = "fallbackPagosPorMatricula")
    ResponseEntity<List<PagoResumenDto>> listarPagosPorMatricula(
            @PathVariable("matriculaId") Long matriculaId
    );

    default ResponseEntity<List<PagoResumenDto>> fallbackPagosPorMatricula(
            Long matriculaId,
            Throwable e
    ) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.emptyList());
    }

    @GetMapping("/resumen-cuotas/{matriculaId}")
    @CircuitBreaker(name = "resumenCuotasPorMatriculaCB", fallbackMethod = "fallbackResumenCuotas")
    ResponseEntity<ResumenCuotasDto> obtenerResumenCuotas(
            @PathVariable("matriculaId") Long matriculaId
    );

    default ResponseEntity<ResumenCuotasDto> fallbackResumenCuotas(
            Long matriculaId,
            Throwable e
    ) {
        ResumenCuotasDto dto = ResumenCuotasDto.builder()
                .matriculaId(matriculaId)
                .cuotasPagadas(0)
                .cuotasPendientes(0)
                .totalPagado(BigDecimal.ZERO)
                .totalPendiente(BigDecimal.ZERO)
                .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }

}
