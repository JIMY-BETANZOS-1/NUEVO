// src/main/java/pe/edu/upeu/msfinanzas/controller/EgresoController.java
package pe.edu.upeu.msfinanzas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msfinanzas.dto.EgresoRequestDto;
import pe.edu.upeu.msfinanzas.dto.EgresoResponseDto;
import pe.edu.upeu.msfinanzas.dto.ResumenCategoriaEgresoDto;
import pe.edu.upeu.msfinanzas.service.EgresoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/egresos")
public class EgresoController {

    @Autowired
    private EgresoService egresoService;

    @PostMapping
    public ResponseEntity<EgresoResponseDto> registrar(@RequestBody EgresoRequestDto dto) {
        return ResponseEntity.ok(egresoService.registrarEgreso(dto));
    }

    @GetMapping
    public ResponseEntity<List<EgresoResponseDto>> listarRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(egresoService.listarEgresosRango(inicio, fin));
    }

    // 🔥 total de egresos en el rango
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> totalEgresos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(egresoService.totalEgresosRango(inicio, fin));
    }

    // 🔥 resumen por categoría
    @GetMapping("/resumen-categorias")
    public ResponseEntity<List<ResumenCategoriaEgresoDto>> resumenCategorias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(egresoService.resumenPorCategoria(inicio, fin));
    }
}
