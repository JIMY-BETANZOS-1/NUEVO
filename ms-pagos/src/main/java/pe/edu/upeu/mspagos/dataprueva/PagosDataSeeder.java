package pe.edu.upeu.mspagos.dataprueva;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.mspagos.entity.*;

import pe.edu.upeu.mspagos.repository.MensualidadRepository;
import pe.edu.upeu.mspagos.repository.PagoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PagosDataSeeder implements CommandLineRunner {

    private final PagoRepository pagoRepository;
    private final MensualidadRepository mensualidadRepository;

    public PagosDataSeeder(PagoRepository pagoRepository,
                           MensualidadRepository mensualidadRepository) {
        this.pagoRepository = pagoRepository;
        this.mensualidadRepository = mensualidadRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (pagoRepository.count() > 0 || mensualidadRepository.count() > 0) {
            return;
        }

        // =========================
        // ⭐ Alumno 1 -> Matrícula ID = 1 (2024) TODO PAGADO
        // =========================
        Long matriculaId2024Alumno1 = 1L;
        int anio = 2024;

        // 1) Mensualidades (MARZO - DICIEMBRE) PAGADAS
        Mensualidad mar = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.MARZO)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 3, 5))
                        .fechaPago(LocalDate.of(2024, 3, 3))
                        .build()
        );
        Mensualidad abr = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.ABRIL)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 4, 5))
                        .fechaPago(LocalDate.of(2024, 4, 4))
                        .build()
        );
        Mensualidad may = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.MAYO)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 5, 5))
                        .fechaPago(LocalDate.of(2024, 5, 4))
                        .build()
        );
        Mensualidad jun = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.JUNIO)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 6, 5))
                        .fechaPago(LocalDate.of(2024, 6, 3))
                        .build()
        );
        Mensualidad jul = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.JULIO)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 7, 5))
                        .fechaPago(LocalDate.of(2024, 7, 4))
                        .build()
        );
        Mensualidad ago = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.AGOSTO)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 8, 5))
                        .fechaPago(LocalDate.of(2024, 8, 3))
                        .build()
        );
        Mensualidad set = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.SETIEMBRE)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 9, 5))
                        .fechaPago(LocalDate.of(2024, 9, 4))
                        .build()
        );
        Mensualidad oct = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.OCTUBRE)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 10, 5))
                        .fechaPago(LocalDate.of(2024, 10, 3))
                        .build()
        );
        Mensualidad nov = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.NOVIEMBRE)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 11, 5))
                        .fechaPago(LocalDate.of(2024, 11, 4))
                        .build()
        );
        Mensualidad dic = mensualidadRepository.save(
                Mensualidad.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .anioEscolar(anio)
                        .mes(MesMensualidad.DICIEMBRE)
                        .monto(new BigDecimal("120.00"))
                        .estado(EstadoPago.PAGADO)
                        .fechaVencimiento(LocalDate.of(2024, 12, 5))
                        .fechaPago(LocalDate.of(2024, 12, 3))
                        .build()
        );

        // 2) Pago de matrícula 2024 (PAGADO)
        Pago pagoMatricula = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MAT-2024-0001-INI")
                        .monto(new BigDecimal("150.00"))
                        .tipoPago(TipoPago.MATRICULA)
                        .metodoPago(MetodoPago.YAPE)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago de matrícula 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 2, 10, 10, 0))
                        .build()
        );
        // ID esperado: 1

        // 3) Pagos de todas las mensualidades (PAGADO)
        Pago pagoMar = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-MAR")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.YAPE)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad MARZO 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 3, 3, 8, 30))
                        .mensualidadId(mar.getId())
                        .build()
        ); // ID esperado: 2

        Pago pagoAbr = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-ABR")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.YAPE)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad ABRIL 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 4, 4, 9, 0))
                        .mensualidadId(abr.getId())
                        .build()
        ); // ID esperado: 3

        Pago pagoMay = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-MAY")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.EFECTIVO)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad MAYO 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 5, 4, 9, 15))
                        .mensualidadId(may.getId())
                        .build()
        ); // ID esperado: 4

        Pago pagoJun = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-JUN")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.EFECTIVO)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad JUNIO 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 6, 3, 9, 30))
                        .mensualidadId(jun.getId())
                        .build()
        ); // ID esperado: 5

        Pago pagoJul = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-JUL")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.YAPE)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad JULIO 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 7, 4, 9, 45))
                        .mensualidadId(jul.getId())
                        .build()
        ); // ID esperado: 6

        Pago pagoAgo = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-AGO")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.PLIN)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad AGOSTO 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 8, 3, 10, 0))
                        .mensualidadId(ago.getId())
                        .build()
        ); // ID esperado: 7

        Pago pagoSet = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-SET")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.BANCO)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad SETIEMBRE 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 9, 4, 10, 10))
                        .mensualidadId(set.getId())
                        .build()
        ); // ID esperado: 8

        Pago pagoOct = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-OCT")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.BANCO)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad OCTUBRE 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 10, 3, 10, 20))
                        .mensualidadId(oct.getId())
                        .build()
        ); // ID esperado: 9

        Pago pagoNov = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-NOV")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.TARJETA)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad NOVIEMBRE 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 11, 4, 10, 30))
                        .mensualidadId(nov.getId())
                        .build()
        ); // ID esperado: 10

        Pago pagoDic = pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno1)
                        .codigoPago("MEN-2024-0001-DIC")
                        .monto(new BigDecimal("120.00"))
                        .tipoPago(TipoPago.MENSUALIDAD)
                        .metodoPago(MetodoPago.TARJETA)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago mensualidad DICIEMBRE 2024 - Alumno 1")
                        .fechaRegistro(LocalDateTime.of(2024, 12, 3, 10, 40))
                        .mensualidadId(dic.getId())
                        .build()
        ); // ID esperado: 11

        // =========================
        // Datos extra para otras matrículas (solo para tener "al menos 3 datos")
        // =========================

        // Matrícula 2 (ID = 2): solo concepto de matrícula pendiente
        Long matriculaId2024Alumno2 = 2L;
        pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2024Alumno2)
                        .codigoPago("MAT-2024-0002-INI")
                        .monto(new BigDecimal("150.00"))
                        .tipoPago(TipoPago.MATRICULA)
                        .metodoPago(null)
                        .estado(EstadoPago.PENDIENTE)
                        .descripcion("Pago de matrícula 2024 pendiente - Alumno 2")
                        .fechaRegistro(LocalDateTime.of(2024, 2, 15, 9, 0))
                        .build()
        );

        // Matrícula 3 (ID = 3, año 2025): un pago de matrícula ya pagado
        Long matriculaId2025Alumno3 = 3L;
        pagoRepository.save(
                Pago.builder()
                        .matriculaId(matriculaId2025Alumno3)
                        .codigoPago("MAT-2025-0001-INI")
                        .monto(new BigDecimal("160.00"))
                        .tipoPago(TipoPago.MATRICULA)
                        .metodoPago(MetodoPago.EFECTIVO)
                        .estado(EstadoPago.PAGADO)
                        .descripcion("Pago de matrícula 2025 - Alumno 3")
                        .fechaRegistro(LocalDateTime.of(2025, 1, 10, 10, 0))
                        .build()
        );
    }
}
