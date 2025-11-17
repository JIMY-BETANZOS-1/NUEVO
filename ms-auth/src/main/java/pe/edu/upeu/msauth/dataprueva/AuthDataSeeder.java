package pe.edu.upeu.msauth.dataprueva;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.msauth.entity.Alumno;
import pe.edu.upeu.msauth.entity.Apoderado;
import pe.edu.upeu.msauth.entity.AuthUser;
import pe.edu.upeu.msauth.repository.AlumnoRepository;
import pe.edu.upeu.msauth.repository.ApoderadoRepository;
import pe.edu.upeu.msauth.repository.AuthUserRepository;

import java.time.LocalDate;

@Component
public class AuthDataSeeder implements CommandLineRunner {

    private final AuthUserRepository authUserRepository;
    private final AlumnoRepository alumnoRepository;
    private final ApoderadoRepository apoderadoRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDataSeeder(AuthUserRepository authUserRepository,
                          AlumnoRepository alumnoRepository,
                          ApoderadoRepository apoderadoRepository,
                          PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.alumnoRepository = alumnoRepository;
        this.apoderadoRepository = apoderadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Si ya hay alumnos o apoderados, no volvemos a sembrar
        if (alumnoRepository.count() > 0 || apoderadoRepository.count() > 0) {
            return;
        }

        // ====== USUARIOS DE LOGIN (AuthUser) ======
        // Password de ejemplo para todos: 123456
        AuthUser uAlumno1 = createUserIfNotExists("alumno1", "123456");
        AuthUser uAlumno2 = createUserIfNotExists("alumno2", "123456");
        AuthUser uAlumno3 = createUserIfNotExists("alumno3", "123456");

        AuthUser uApo1 = createUserIfNotExists("apo1", "123456");
        AuthUser uApo2 = createUserIfNotExists("apo2", "123456");
        AuthUser uApo3 = createUserIfNotExists("apo3", "123456");

        // ====== ALUMNOS ======
        // ⭐ Alumno 1: el que tendrá TODO pagado en 2024 (coincide con matriculaId=1)
        Alumno a1 = Alumno.builder()
                .nombres("Juan Carlos")
                .apellidos("Pérez López")
                .dni("12345678") // usado en ms-finanzas y ms-matriculas para búsquedas por DNI
                .fechaNacimiento(LocalDate.of(2012, 5, 10))
                .gradoActual("5°")
                .seccionActual("A")
                .activo(true)
                .authUser(uAlumno1)        // userName = alumno1
                .build();
        a1 = alumnoRepository.save(a1); // ID esperado: 1

        // Alumno 2: otra matrícula 2024 (parcialmente pagada en ms-pagos)
        Alumno a2 = Alumno.builder()
                .nombres("María Fernanda")
                .apellidos("Rojas Salazar")
                .dni("23456789")
                .fechaNacimiento(LocalDate.of(2013, 3, 22))
                .gradoActual("4°")
                .seccionActual("B")
                .activo(true)
                .authUser(uAlumno2)        // userName = alumno2
                .build();
        a2 = alumnoRepository.save(a2); // ID esperado: 2

        // Alumno 3: matrícula 2025
        Alumno a3 = Alumno.builder()
                .nombres("Luis Alberto")
                .apellidos("García Mendoza")
                .dni("34567890")
                .fechaNacimiento(LocalDate.of(2014, 8, 15))
                .gradoActual("3°")
                .seccionActual("C")
                .activo(true)
                .authUser(uAlumno3)        // userName = alumno3
                .build();
        a3 = alumnoRepository.save(a3); // ID esperado: 3

        // ====== APODERADOS ======
        // Apoderado 1 del Alumno 1 (usado para búsquedas por DNI de apoderado)
        Apoderado ap1 = Apoderado.builder()
                .nombres("Carlos")
                .apellidos("Pérez Ramírez")
                .dni("87654321") // usado en ms-matriculas.listarPorDniApoderado
                .telefono("999111222")
                .parentesco("Padre")
                .alumno(a1)
                .authUser(uApo1)          // userName = apo1
                .build();
        apoderadoRepository.save(ap1);

        // Apoderado 2 del Alumno 2
        Apoderado ap2 = Apoderado.builder()
                .nombres("Ana")
                .apellidos("Rojas Díaz")
                .dni("98765432")
                .telefono("988222333")
                .parentesco("Madre")
                .alumno(a2)
                .authUser(uApo2)          // userName = apo2
                .build();
        apoderadoRepository.save(ap2);

        // Apoderado 3 del Alumno 3
        Apoderado ap3 = Apoderado.builder()
                .nombres("Miguel")
                .apellidos("García Torres")
                .dni("99665544")
                .telefono("977333444")
                .parentesco("Padre")
                .alumno(a3)
                .authUser(uApo3)          // userName = apo3
                .build();
        apoderadoRepository.save(ap3);
    }

    // ====== Helpers ======

    private AuthUser createUserIfNotExists(String userName, String rawPassword) {
        return authUserRepository.findByUserName(userName)
                .orElseGet(() -> {
                    String enc = passwordEncoder.encode(rawPassword);
                    AuthUser u = AuthUser.builder()
                            .userName(userName)
                            .password(enc)
                            .build();
                    return authUserRepository.save(u);
                });
    }
}
