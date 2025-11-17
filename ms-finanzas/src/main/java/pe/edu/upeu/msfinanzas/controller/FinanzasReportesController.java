package pe.edu.upeu.msfinanzas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msfinanzas.dto.*;
import pe.edu.upeu.msfinanzas.feign.AlumnoFinanzasFeignClient;
import pe.edu.upeu.msfinanzas.feign.MatriculaFinanzasFeignClient;
import pe.edu.upeu.msfinanzas.service.EgresoService;
import pe.edu.upeu.msfinanzas.service.IngresoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/finanzas")
public class FinanzasReportesController {

    @Autowired
    private IngresoService ingresoService;

    @Autowired
    private EgresoService egresoService;

    @Autowired
    private AlumnoFinanzasFeignClient alumnoFeignClient;

    @Autowired
    private MatriculaFinanzasFeignClient matriculaFinanzasFeignClient;

    @Autowired
    private pe.edu.upeu.msfinanzas.feign.PagoFeignClient pagoFeignClient;

    // 1) Resumen completo ingresos vs egresos en un rango
    @GetMapping("/resumen-completo")
    public ResponseEntity<ResumenFinancieroDto> resumenCompleto(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        ResumenFinancieroDto ingresos = ingresoService.resumenRango(inicio, fin);
        var egresos = egresoService.listarEgresosRango(inicio, fin);

        BigDecimal totalEgresos = egresos.stream()
                .map(EgresoResponseDto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = ingresos.getTotalIngresos().subtract(totalEgresos);

        ResumenFinancieroDto resultado = ResumenFinancieroDto.builder()
                .totalIngresos(ingresos.getTotalIngresos())
                .totalEgresos(totalEgresos)
                .saldo(saldo)
                .build();

        return ResponseEntity.ok(resultado);
    }

    // 2) Resumen mensual por año (12 filas)
    @GetMapping("/resumen-mensual")
    public ResponseEntity<List<ResumenMensualDto>> resumenMensual(
            @RequestParam Integer anio
    ) {
        List<ResumenMensualDto> lista = new ArrayList<>();

        for (int mes = 1; mes <= 12; mes++) {
            LocalDate inicio = LocalDate.of(anio, mes, 1);
            LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

            var ingresosMes = ingresoService.listarIngresosRango(inicio, fin);
            var egresosMes = egresoService.listarEgresosRango(inicio, fin);

            BigDecimal totalIngresos = ingresosMes.stream()
                    .map(IngresoResponseDto::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalEgresos = egresosMes.stream()
                    .map(EgresoResponseDto::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            lista.add(ResumenMensualDto.builder()
                    .mes(mes)
                    .totalIngresos(totalIngresos)
                    .totalEgresos(totalEgresos)
                    .saldo(totalIngresos.subtract(totalEgresos))
                    .build());
        }

        return ResponseEntity.ok(lista);
    }

    // 3) Resumen de egresos por categoría en un rango
    @GetMapping("/egresos/resumen-categorias")
    public ResponseEntity<List<ResumenCategoriaEgresoDto>> resumenEgresosPorCategoria(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        var egresos = egresoService.listarEgresosRango(inicio, fin);

        Map<String, BigDecimal> mapa = new HashMap<>();

        for (EgresoResponseDto e : egresos) {
            String cat = e.getCategoria().name();
            BigDecimal monto = e.getMonto() != null ? e.getMonto() : BigDecimal.ZERO;
            mapa.merge(cat, monto, BigDecimal::add);
        }

        List<ResumenCategoriaEgresoDto> res = mapa.entrySet().stream()
                .map(en -> ResumenCategoriaEgresoDto.builder()
                        .categoria(en.getKey())
                        .total(en.getValue())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    // 4) Historial financiero por DNI de alumno
    @GetMapping("/historial/alumno/{dni}")
    public ResponseEntity<HistorialAlumnoDto> historialAlumnoPorDni(
            @PathVariable String dni
    ) {
        // 1. Alumno desde ms-auth
        var respAlumno = alumnoFeignClient.buscarPorDni(dni);
        AlumnoFeignDto alumno = respAlumno.getBody();

        if (alumno == null || alumno.getId() == null) {
            throw new RuntimeException("No se encontró alumno con DNI " + dni);
        }

        // 2. Matrículas del alumno desde ms-matriculas
        var respMats = matriculaFinanzasFeignClient.listarPorAlumno(alumno.getId());
        List<MatriculaListadoDto> mats = respMats.getBody();
        if (mats == null) mats = List.of();

        List<HistorialMatriculaDto> matsHist = new ArrayList<>();

        for (MatriculaListadoDto m : mats) {
            // pagos por matrícula
            var respPagos = pagoFeignClient.listarPagosPorMatricula(m.getId());
            List<PagoResumenDto> pagos = respPagos.getBody();
            if (pagos == null) pagos = List.of();

            // resumen de cuotas
            var respRes = pagoFeignClient.obtenerResumenCuotas(m.getId());
            ResumenCuotasDto resumen = respRes.getBody();

            matsHist.add(HistorialMatriculaDto.builder()
                    .matriculaId(m.getId())
                    .codigoMatricula(m.getCodigoMatricula())
                    .anioEscolar(m.getAnioEscolar())
                    .grado(m.getGrado())
                    .seccion(m.getSeccion())
                    .resumenCuotas(resumen)
                    .pagos(pagos)
                    .build());
        }

        HistorialAlumnoDto dto = HistorialAlumnoDto.builder()
                .dni(dni)
                .nombres(alumno.getNombres())
                .apellidos(alumno.getApellidos())
                .matriculas(matsHist)
                .build();

        return ResponseEntity.ok(dto);
    }
}
