package pe.edu.upeu.msmatriculas.feign;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msmatriculas.dto.AlumnoResponseDto;

@FeignClient(name = "ms-auth", path = "/alumnos", contextId = "alumnoFeignClient")
public interface AlumnoFeignClient {

    @GetMapping("/by-username/{userName}")
    @CircuitBreaker(name = "alumnoBuscarPorUserCB", fallbackMethod = "fallbackBuscarAlumnoPorUserName")
    ResponseEntity<AlumnoResponseDto> buscarAlumnoPorUserName(@PathVariable("userName") String userName);

    // 👇 NUEVO: obtener alumno por ID (ms-auth ya lo expone)
    @GetMapping("/{id}")
    @CircuitBreaker(name = "alumnoBuscarPorIdCB", fallbackMethod = "fallbackBuscarAlumnoPorId")
    ResponseEntity<AlumnoResponseDto> buscarAlumnoPorId(@PathVariable("id") Integer id);

    @GetMapping("/by-dni/{dni}")
    @CircuitBreaker(name = "alumnoBuscarPorDniCB", fallbackMethod = "fallbackBuscarAlumnoPorDni")
    ResponseEntity<AlumnoResponseDto> buscarAlumnoPorDni(@PathVariable("dni") String dni);


    // Fallback cuando ms-auth está caído (by username)
    default ResponseEntity<AlumnoResponseDto> fallbackBuscarAlumnoPorUserName(String userName, Throwable e) {
        AlumnoResponseDto dto = new AlumnoResponseDto();
        dto.setId(null);
        dto.setNombres("SERVICIO AUTH NO DISPONIBLE");
        dto.setApellidos("");
        dto.setUserName(userName);
        dto.setActivo(false);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }

    // 👇 NUEVO fallback por ID
    default ResponseEntity<AlumnoResponseDto> fallbackBuscarAlumnoPorId(Integer id, Throwable e) {
        AlumnoResponseDto dto = new AlumnoResponseDto();
        dto.setId(id);
        dto.setNombres("SERVICIO AUTH NO DISPONIBLE");
        dto.setApellidos("");
        dto.setActivo(false);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }

    // Fallback nuevo
    default ResponseEntity<AlumnoResponseDto> fallbackBuscarAlumnoPorDni(String dni, Throwable e) {
        AlumnoResponseDto dto = new AlumnoResponseDto();
        dto.setId(null);
        dto.setNombres("SERVICIO AUTH NO DISPONIBLE");
        dto.setApellidos("");
        dto.setDni(dni);
        dto.setActivo(false);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(dto);
    }

    @PutMapping("/{id}/grado-seccion")
    @CircuitBreaker(
            name = "alumnoActualizarGradoSeccionCB",
            fallbackMethod = "fallbackActualizarGradoSeccion"
    )
    ResponseEntity<Void> actualizarGradoSeccion(
            @PathVariable("id") Integer id,
            @RequestParam("grado") String grado,
            @RequestParam("seccion") String seccion
    );

    default ResponseEntity<Void> fallbackActualizarGradoSeccion(
            Integer id,
            String grado,
            String seccion,
            Throwable e
    ) {
        // Si ms-auth está caído, no rompemos nada, solo devolvemos 503
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}
