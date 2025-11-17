package pe.edu.upeu.msfinanzas.dataprueva;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msfinanzas.entity.CategoriaEgreso;
import pe.edu.upeu.msfinanzas.entity.Egreso;
import pe.edu.upeu.msfinanzas.entity.Ingreso;
import pe.edu.upeu.msfinanzas.repository.EgresoRepository;
import pe.edu.upeu.msfinanzas.repository.IngresoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class FinanzasDataSeeder implements CommandLineRunner {

    private final IngresoRepository ingresoRepository;
    private final EgresoRepository egresoRepository;

    public FinanzasDataSeeder(IngresoRepository ingresoRepository,
                              EgresoRepository egresoRepository) {
        this.ingresoRepository = ingresoRepository;
        this.egresoRepository = egresoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (ingresoRepository.count() > 0 || egresoRepository.count() > 0) {
            return;
        }

        // =========================
        // ⭐ Ingresos por TODOS los pagos de Alumno 1 en 2024
        //    (coinciden con los IDs de ms-pagos)
        // =========================

        // pagoId 1: matrícula 2024
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(1L)
                        .descripcion("Ingreso por matrícula 2024 - Alumno 1")
                        .monto(new BigDecimal("150.00"))
                        .origen("MATRICULA")
                        .fecha(LocalDateTime.of(2024, 2, 10, 10, 5))
                        .build()
        );

        // pagoId 2..11: mensualidades marzo-diciembre 2024
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(2L)
                        .descripcion("Ingreso mensualidad MARZO 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 3, 3, 8, 35))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(3L)
                        .descripcion("Ingreso mensualidad ABRIL 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 4, 4, 9, 5))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(4L)
                        .descripcion("Ingreso mensualidad MAYO 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 5, 4, 9, 20))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(5L)
                        .descripcion("Ingreso mensualidad JUNIO 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 6, 3, 9, 35))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(6L)
                        .descripcion("Ingreso mensualidad JULIO 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 7, 4, 9, 50))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(7L)
                        .descripcion("Ingreso mensualidad AGOSTO 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 8, 3, 10, 5))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(8L)
                        .descripcion("Ingreso mensualidad SETIEMBRE 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 9, 4, 10, 15))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(9L)
                        .descripcion("Ingreso mensualidad OCTUBRE 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 10, 3, 10, 25))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(10L)
                        .descripcion("Ingreso mensualidad NOVIEMBRE 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 11, 4, 10, 35))
                        .build()
        );
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(11L)
                        .descripcion("Ingreso mensualidad DICIEMBRE 2024 - Alumno 1")
                        .monto(new BigDecimal("120.00"))
                        .origen("MENSUALIDAD")
                        .fecha(LocalDateTime.of(2024, 12, 3, 10, 45))
                        .build()
        );

        // Ingreso extra: matrícula 2025 alumno 3 (pagoId ~ 13 aprox, según seeder de ms-pagos)
        ingresoRepository.save(
                Ingreso.builder()
                        .pagoId(13L) // Según el orden de inserción en ms-pagos
                        .descripcion("Ingreso matrícula 2025 - Alumno 3")
                        .monto(new BigDecimal("160.00"))
                        .origen("MATRICULA")
                        .fecha(LocalDateTime.of(2025, 1, 10, 10, 5))
                        .build()
        );

        // =========================
        // Egresos de ejemplo (servicios, sueldos, materiales)
        // =========================

        egresoRepository.save(
                Egreso.builder()
                        .descripcion("Pago de servicios de luz y agua - marzo 2024")
                        .monto(new BigDecimal("300.00"))
                        .categoria(CategoriaEgreso.SERVICIOS)
                        .fecha(LocalDateTime.of(2024, 3, 20, 12, 0))
                        .build()
        );

        egresoRepository.save(
                Egreso.builder()
                        .descripcion("Pago de sueldos docentes - marzo 2024")
                        .monto(new BigDecimal("5000.00"))
                        .categoria(CategoriaEgreso.SUELDOS)
                        .fecha(LocalDateTime.of(2024, 3, 30, 18, 0))
                        .build()
        );

        egresoRepository.save(
                Egreso.builder()
                        .descripcion("Compra de materiales de laboratorio")
                        .monto(new BigDecimal("800.00"))
                        .categoria(CategoriaEgreso.MATERIALES)
                        .fecha(LocalDateTime.of(2024, 4, 10, 11, 30))
                        .build()
        );
    }
}
