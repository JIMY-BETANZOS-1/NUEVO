package pe.edu.upeu.mspagos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.mspagos.dto.MensualidadResponseDto;
import pe.edu.upeu.mspagos.dto.PagoRequestDto;
import pe.edu.upeu.mspagos.dto.PagoResponseDto;
import pe.edu.upeu.mspagos.dto.ResumenCuotasDto;
import pe.edu.upeu.mspagos.service.PagoService;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // Generar pago de matrícula + mensualidades para una matrícula
    @PostMapping("/inicial/{matriculaId}")
    public ResponseEntity<Void> generarConceptosIniciales(
            @PathVariable Long matriculaId,
            @RequestParam Integer anioEscolar
    ) {
        pagoService.generarConceptosIniciales(matriculaId, anioEscolar);
        return ResponseEntity.ok().build();
    }

    // Registrar un pago (matrícula, mensualidad, otros)
    @PostMapping
    public ResponseEntity<PagoResponseDto> registrarPago(@RequestBody PagoRequestDto request) {
        return ResponseEntity.ok(pagoService.registrarPago(request));
    }

    // Listar pagos por matrícula
    @GetMapping("/matricula/{matriculaId}")
    public ResponseEntity<List<PagoResponseDto>> listarPagosPorMatricula(@PathVariable Long matriculaId) {
        return ResponseEntity.ok(pagoService.listarPagosPorMatricula(matriculaId));
    }

    // Listar mensualidades por matrícula
    @GetMapping("/mensualidades/{matriculaId}")
    public ResponseEntity<List<MensualidadResponseDto>> listarMensualidadesPorMatricula(
            @PathVariable Long matriculaId
    ) {
        return ResponseEntity.ok(pagoService.listarMensualidadesPorMatricula(matriculaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    // Resumen de cuotas (mensualidades) por matrícula
    @GetMapping("/resumen-cuotas/{matriculaId}")
    public ResponseEntity<ResumenCuotasDto> obtenerResumenCuotas(
            @PathVariable Long matriculaId
    ) {
        return ResponseEntity.ok(pagoService.obtenerResumenCuotasPorMatricula(matriculaId));
    }

    // Resumen de cuotas por código de matrícula
    @GetMapping("/resumen-cuotas/codigo/{codigoMatricula}")
    public ResponseEntity<ResumenCuotasDto> obtenerResumenCuotasPorCodigo(
            @PathVariable String codigoMatricula
    ) {
        return ResponseEntity.ok(pagoService.obtenerResumenCuotasPorCodigoMatricula(codigoMatricula));
    }

}
