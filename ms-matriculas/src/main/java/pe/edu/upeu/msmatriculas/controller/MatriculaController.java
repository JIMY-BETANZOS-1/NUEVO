package pe.edu.upeu.msmatriculas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msmatriculas.dto.MatriculaRequestDto;
import pe.edu.upeu.msmatriculas.dto.MatriculaResponseDto;
import pe.edu.upeu.msmatriculas.entity.EstadoMatricula;
import pe.edu.upeu.msmatriculas.service.MatriculaService;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    // Crear matrícula para el alumno logueado
    @PostMapping
    public ResponseEntity<MatriculaResponseDto> crear(@RequestBody MatriculaRequestDto request) {
        return ResponseEntity.ok(matriculaService.crearMatricula(request));
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDto>> listar() {
        return ResponseEntity.ok(matriculaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<MatriculaResponseDto>> listarPorAlumno(@PathVariable Integer alumnoId) {
        return ResponseEntity.ok(matriculaService.listarPorAlumno(alumnoId));
    }

    @GetMapping("/anio/{anioEscolar}")
    public ResponseEntity<List<MatriculaResponseDto>> listarPorAnio(@PathVariable Integer anioEscolar) {
        return ResponseEntity.ok(matriculaService.listarPorAnio(anioEscolar));
    }

    // Cambiar estado a RETIRADO
    @PutMapping("/{id}/retirar")
    public ResponseEntity<MatriculaResponseDto> retirar(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.cambiarEstado(id, EstadoMatricula.RETIRADO));
    }

    // Cambiar estado a ANULADO
    @PutMapping("/{id}/anular")
    public ResponseEntity<MatriculaResponseDto> anular(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.cambiarEstado(id, EstadoMatricula.ANULADO));
    }

    @GetMapping("/codigo/{codigoMatricula}")
    public ResponseEntity<MatriculaResponseDto> obtenerPorCodigo(
            @PathVariable String codigoMatricula) {
        return ResponseEntity.ok(matriculaService.obtenerPorCodigo(codigoMatricula));
    }

    // Buscar matrículas por DNI del alumno
    @GetMapping("/buscar/alumno-dni/{dni}")
    public ResponseEntity<List<MatriculaResponseDto>> listarPorDniAlumno(@PathVariable String dni) {
        return ResponseEntity.ok(matriculaService.listarPorDniAlumno(dni));
    }

    // Buscar matrículas por DNI del apoderado
    @GetMapping("/buscar/apoderado-dni/{dni}")
    public ResponseEntity<List<MatriculaResponseDto>> listarPorDniApoderado(@PathVariable String dni) {
        return ResponseEntity.ok(matriculaService.listarPorDniApoderado(dni));
    }


}
