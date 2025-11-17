package pe.edu.upeu.msauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msauth.dto.ApoderadoRequestDto;
import pe.edu.upeu.msauth.dto.ApoderadoResponseDto;
import pe.edu.upeu.msauth.entity.Alumno;
import pe.edu.upeu.msauth.entity.Apoderado;
import pe.edu.upeu.msauth.entity.AuthUser;
import pe.edu.upeu.msauth.repository.AlumnoRepository;
import pe.edu.upeu.msauth.repository.ApoderadoRepository;
import pe.edu.upeu.msauth.repository.AuthUserRepository;
import pe.edu.upeu.msauth.service.ApoderadoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApoderadoServiceImpl implements ApoderadoService {

    @Autowired
    private ApoderadoRepository apoderadoRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ApoderadoResponseDto crearApoderado(ApoderadoRequestDto dto) {

        if (dto.getAlumnoId() == null) {
            throw new RuntimeException("alumnoId es obligatorio para crear el apoderado");
        }

        AuthUser authUser = crearOReutilizarAuthUser(dto.getUserName(), dto.getPassword());

        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + dto.getAlumnoId()));

        Apoderado apoderado = Apoderado.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .dni(dto.getDni())
                .telefono(dto.getTelefono())
                .parentesco(dto.getParentesco())
                .alumno(alumno)
                .authUser(authUser)
                .build();

        Apoderado guardado = apoderadoRepository.save(apoderado);
        return mapToResponse(guardado);
    }


    @Override
    public ApoderadoResponseDto obtenerPorId(Integer id) {
        Apoderado apoderado = apoderadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado"));
        return mapToResponse(apoderado);
    }

    @Override
    public List<ApoderadoResponseDto> listarTodos() {
        return apoderadoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApoderadoResponseDto> listarPorAlumno(Integer alumnoId) {
        return apoderadoRepository.findByAlumno_Id(alumnoId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApoderadoResponseDto buscarPorUserName(String userName) {
        Apoderado apoderado = apoderadoRepository.findByAuthUser_UserName(userName)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado para el usuario: " + userName));
        return mapToResponse(apoderado);
    }

    @Override
    public ApoderadoResponseDto actualizarApoderado(Integer id, ApoderadoRequestDto dto) {
        Apoderado apoderado = apoderadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado"));

        apoderado.setNombres(dto.getNombres());
        apoderado.setApellidos(dto.getApellidos());
        apoderado.setDni(dto.getDni());
        apoderado.setTelefono(dto.getTelefono());
        apoderado.setParentesco(dto.getParentesco());

        if (dto.getAlumnoId() != null) {
            Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
            apoderado.setAlumno(alumno);
        }

        Apoderado actualizado = apoderadoRepository.save(apoderado);
        return mapToResponse(actualizado);
    }

    @Override
    public void eliminarApoderado(Integer id) {
        apoderadoRepository.deleteById(id);
    }

    // ========= Helpers =========

    private AuthUser crearOReutilizarAuthUser(String userName, String password) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new RuntimeException("userName es obligatorio para el login del apoderado");
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

    private ApoderadoResponseDto mapToResponse(Apoderado apoderado) {
        Integer alumnoId = apoderado.getAlumno() != null ? apoderado.getAlumno().getId() : null;
        String alumnoNombreCompleto = null;
        if (apoderado.getAlumno() != null) {
            alumnoNombreCompleto = apoderado.getAlumno().getNombres() + " " + apoderado.getAlumno().getApellidos();
        }

        Integer authUserId = apoderado.getAuthUser() != null ? apoderado.getAuthUser().getId() : null;
        String userName = apoderado.getAuthUser() != null ? apoderado.getAuthUser().getUserName() : null;

        return ApoderadoResponseDto.builder()
                .id(apoderado.getId())
                .nombres(apoderado.getNombres())
                .apellidos(apoderado.getApellidos())
                .dni(apoderado.getDni())
                .telefono(apoderado.getTelefono())
                .parentesco(apoderado.getParentesco())
                .alumnoId(alumnoId)
                .alumnoNombreCompleto(alumnoNombreCompleto)
                .authUserId(authUserId)
                .userName(userName)
                .build();
    }
    @Override
    public ApoderadoResponseDto buscarPorDni(String dni) {
        return apoderadoRepository.findByDni(dni)
                .map(this::mapToResponse)
                .orElse(null); // 👈 importante: NADA de orElseThrow aquí
    }





}
