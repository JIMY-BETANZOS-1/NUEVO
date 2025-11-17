package pe.edu.upeu.msauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msauth.dto.ApoderadoRequestDto;
import pe.edu.upeu.msauth.dto.ApoderadoResponseDto;
import pe.edu.upeu.msauth.service.ApoderadoService;

import java.util.List;

@RestController
@RequestMapping("/apoderados")
public class ApoderadoController {

    @Autowired
    private ApoderadoService apoderadoService;

    @PostMapping
    public ResponseEntity<ApoderadoResponseDto> crear(@RequestBody ApoderadoRequestDto dto) {
        return ResponseEntity.ok(apoderadoService.crearApoderado(dto));
    }

    @GetMapping
    public ResponseEntity<List<ApoderadoResponseDto>> listar() {
        return ResponseEntity.ok(apoderadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApoderadoResponseDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(apoderadoService.obtenerPorId(id));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<ApoderadoResponseDto>> listarPorAlumno(@PathVariable Integer alumnoId) {
        return ResponseEntity.ok(apoderadoService.listarPorAlumno(alumnoId));
    }

    @GetMapping("/by-username/{userName}")
    public ResponseEntity<ApoderadoResponseDto> obtenerPorUserName(@PathVariable String userName) {
        return ResponseEntity.ok(apoderadoService.buscarPorUserName(userName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApoderadoResponseDto> actualizar(@PathVariable Integer id,
                                                           @RequestBody ApoderadoRequestDto dto) {
        return ResponseEntity.ok(apoderadoService.actualizarApoderado(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        apoderadoService.eliminarApoderado(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-dni/{dni}")
    public ResponseEntity<ApoderadoResponseDto> obtenerPorDni(@PathVariable String dni) {
        ApoderadoResponseDto dto = apoderadoService.buscarPorDni(dni);
        if (dto == null) {
            return ResponseEntity.notFound().build(); // 404 si no hay
        }
        return ResponseEntity.ok(dto);
    }



}
