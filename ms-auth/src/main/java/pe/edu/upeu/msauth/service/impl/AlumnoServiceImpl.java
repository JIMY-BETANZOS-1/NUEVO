package pe.edu.upeu.msauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msauth.dto.AlumnoRequestDto;
import pe.edu.upeu.msauth.dto.AlumnoResponseDto;
import pe.edu.upeu.msauth.entity.Alumno;
import pe.edu.upeu.msauth.entity.AuthUser;
import pe.edu.upeu.msauth.repository.AlumnoRepository;
import pe.edu.upeu.msauth.repository.AuthUserRepository;
import pe.edu.upeu.msauth.service.AlumnoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AlumnoResponseDto crearAlumno(AlumnoRequestDto dto) {

        AuthUser authUser = crearOReutilizarAuthUser(dto.getUserName(), dto.getPassword());

        LocalDate fechaNac = dto.getFechaNacimiento() != null
                ? LocalDate.parse(dto.getFechaNacimiento())
                : null;

        Alumno alumno = Alumno.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .dni(dto.getDni())
                .fechaNacimiento(fechaNac)
                .gradoActual(dto.getGradoActual())
                .seccionActual(dto.getSeccionActual())
                .activo(true)
                .authUser(authUser)
                .build();

        Alumno guardado = alumnoRepository.save(alumno);
        return mapToResponse(guardado);
    }

    @Override
    public AlumnoResponseDto obtenerPorId(Integer id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        return mapToResponse(alumno);
    }

    @Override
    public List<AlumnoResponseDto> listarTodos() {
        return alumnoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AlumnoResponseDto buscarPorUserName(String userName) {
        Alumno alumno = alumnoRepository.findByAuthUser_UserName(userName)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado para el usuario: " + userName));
        return mapToResponse(alumno);
    }

    @Override
    public AlumnoResponseDto actualizarAlumno(Integer id, AlumnoRequestDto dto) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        alumno.setNombres(dto.getNombres());
        alumno.setApellidos(dto.getApellidos());
        alumno.setDni(dto.getDni());
        alumno.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
        alumno.setGradoActual(dto.getGradoActual());
        alumno.setSeccionActual(dto.getSeccionActual());

        Alumno actualizado = alumnoRepository.save(alumno);
        return mapToResponse(actualizado);
    }

    @Override
    public void eliminarAlumno(Integer id) {
        alumnoRepository.deleteById(id);
    }

    // ================== Helpers ==================

    private AuthUser crearOReutilizarAuthUser(String userName, String password) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new RuntimeException("userName es obligatorio para el login del alumno");
        }

        Optional<AuthUser> existente = authUserRepository.findByUserName(userName);
        if (existente.isPresent()) {
            return existente.get();
        }

        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("password es obligatorio para crear un nuevo usuario");
        }

        String passwordEnc = passwordEncoder.encode(password);

        AuthUser authUser = AuthUser.builder()
                .userName(userName)
                .password(passwordEnc)
                .build();

        return authUserRepository.save(authUser);
    }

    private AlumnoResponseDto mapToResponse(Alumno alumno) {
        Integer authUserId = alumno.getAuthUser() != null ? alumno.getAuthUser().getId() : null;
        String userName = alumno.getAuthUser() != null ? alumno.getAuthUser().getUserName() : null;

        return AlumnoResponseDto.builder()
                .id(alumno.getId())
                .nombres(alumno.getNombres())
                .apellidos(alumno.getApellidos())
                .dni(alumno.getDni())
                .fechaNacimiento(alumno.getFechaNacimiento())
                .gradoActual(alumno.getGradoActual())
                .seccionActual(alumno.getSeccionActual())
                .activo(alumno.getActivo())
                .authUserId(authUserId)
                .userName(userName)
                .build();
    }
    @Override
    public AlumnoResponseDto buscarPorDni(String dni) {
        Alumno alumno = alumnoRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado para DNI: " + dni));
        return mapToResponse(alumno);
    }

    @Override
    public AlumnoResponseDto actualizarGradoSeccion(Integer id, String grado, String seccion) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        alumno.setGradoActual(grado);
        alumno.setSeccionActual(seccion);

        Alumno guardado = alumnoRepository.save(alumno);

        // Mapea a AlumnoResponseDto (usa tu mapper actual)
        return AlumnoResponseDto.builder()
                .id(guardado.getId())
                .nombres(guardado.getNombres())
                .apellidos(guardado.getApellidos())
                .dni(guardado.getDni())
                .fechaNacimiento(guardado.getFechaNacimiento())
                .gradoActual(guardado.getGradoActual())
                .seccionActual(guardado.getSeccionActual())
                .activo(guardado.getActivo())
                .authUserId(guardado.getAuthUser().getId())
                .userName(guardado.getAuthUser().getUserName())
                .build();
    }


}
