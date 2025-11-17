// src/main/java/pe/edu/upeu/msfinanzas/controller/IngresoController.java
package pe.edu.upeu.msfinanzas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msfinanzas.dto.IngresoRequestDto;
import pe.edu.upeu.msfinanzas.dto.IngresoResponseDto;
import pe.edu.upeu.msfinanzas.dto.ResumenFinancieroDto;
import pe.edu.upeu.msfinanzas.service.IngresoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ingresos")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    // Registrar ingreso (manual o desde otra parte si quisieras)
    @PostMapping
    public ResponseEntity<IngresoResponseDto> registrar(@RequestBody IngresoRequestDto dto) {
        return ResponseEntity.ok(ingresoService.registrarIngreso(dto));
    }

    // Listar ingresos en un rango de fechas
    @GetMapping
    public ResponseEntity<List<IngresoResponseDto>> listarRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(ingresoService.listarIngresosRango(inicio, fin));
    }

    // Resumen solo de ingresos (totalIngresos en ese rango)
    @GetMapping("/resumen")
    public ResponseEntity<ResumenFinancieroDto> resumenIngresos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(ingresoService.resumenRango(inicio, fin));
    }
}
