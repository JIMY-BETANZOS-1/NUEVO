package pe.edu.upeu.msauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msauth.dto.AlumnoRequestDto;
import pe.edu.upeu.msauth.dto.AlumnoResponseDto;
import pe.edu.upeu.msauth.service.AlumnoService;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @PostMapping
    public ResponseEntity<AlumnoResponseDto> crear(@RequestBody AlumnoRequestDto dto) {
        return ResponseEntity.ok(alumnoService.crearAlumno(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlumnoResponseDto>> listar() {
        return ResponseEntity.ok(alumnoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponseDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(alumnoService.obtenerPorId(id));
    }

    @GetMapping("/by-username/{userName}")
    public ResponseEntity<AlumnoResponseDto> obtenerPorUserName(@PathVariable String userName) {
        return ResponseEntity.ok(alumnoService.buscarPorUserName(userName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponseDto> actualizar(@PathVariable Integer id,
                                                        @RequestBody AlumnoRequestDto dto) {
        return ResponseEntity.ok(alumnoService.actualizarAlumno(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-dni/{dni}")
    public ResponseEntity<AlumnoResponseDto> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(alumnoService.buscarPorDni(dni));
    }

    @PutMapping("/{id}/grado-seccion")
    public ResponseEntity<AlumnoResponseDto> actualizarGradoSeccion(
            @PathVariable Integer id,
            @RequestParam String grado,
            @RequestParam String seccion
    ) {
        return ResponseEntity.ok(alumnoService.actualizarGradoSeccion(id, grado, seccion));
    }

}
