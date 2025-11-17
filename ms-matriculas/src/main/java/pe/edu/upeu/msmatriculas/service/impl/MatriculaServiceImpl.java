package pe.edu.upeu.msmatriculas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.upeu.msmatriculas.dto.*;
import pe.edu.upeu.msmatriculas.entity.EstadoMatricula;
import pe.edu.upeu.msmatriculas.entity.Matricula;
import pe.edu.upeu.msmatriculas.entity.TipoMatricula;
import pe.edu.upeu.msmatriculas.feign.AlumnoFeignClient;
import pe.edu.upeu.msmatriculas.feign.ApoderadoFeignClient;
import pe.edu.upeu.msmatriculas.feign.PagoFeignClient;
import pe.edu.upeu.msmatriculas.repository.MatriculaRepository;
import pe.edu.upeu.msmatriculas.service.MatriculaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaServiceImpl implements MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlumnoFeignClient alumnoFeignClient;

    @Autowired
    private PagoFeignClient pagoFeignClient;

    @Autowired
    private ApoderadoFeignClient apoderadoFeignClient;

    @Override
    public MatriculaResponseDto crearMatricula(MatriculaRequestDto request) {
        // 1. Validar userName
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            throw new RuntimeException("userName es obligatorio para matricular (usuario logeado).");
        }

        // 2. Obtener info del alumno según usuario logeado (ms-auth)
        ResponseEntity<AlumnoResponseDto> response = alumnoFeignClient.buscarAlumnoPorUserName(request.getUserName());

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null
                || response.getBody().getId() == null) {
            throw new RuntimeException("No se pudo obtener información del alumno desde ms-auth.");
        }

        AlumnoResponseDto alumnoDto = response.getBody();

        if (Boolean.FALSE.equals(alumnoDto.getActivo())) {
            throw new RuntimeException("El alumno no está activo y no puede matricularse.");
        }

        // 3. Regla: evitar dos matrículas activas del mismo año
        List<Matricula> activasMismoAnio = matriculaRepository
                .findByAlumnoIdAndEstado(alumnoDto.getId(), EstadoMatricula.MATRICULADO)
                .stream()
                .filter(m -> m.getAnioEscolar().equals(request.getAnioEscolar()))
                .collect(Collectors.toList());

        if (!activasMismoAnio.isEmpty()) {
            throw new RuntimeException("El alumno ya tiene una matrícula activa en el año " + request.getAnioEscolar());
        }

        // 4. Generar código de matrícula
        String codigoMatricula = generarCodigoMatricula(request.getAnioEscolar());

        // 5. Crear entidad Matrícula
        Matricula matricula = Matricula.builder()
                .codigoMatricula(codigoMatricula)
                .alumnoId(alumnoDto.getId())
                .anioEscolar(request.getAnioEscolar())
                .tipoMatricula(request.getTipoMatricula() != null ? request.getTipoMatricula() : TipoMatricula.CONTINUIDAD)
                .grado(request.getGrado())
                .seccion(request.getSeccion())
                .fechaMatricula(LocalDateTime.now())
                .estado(EstadoMatricula.MATRICULADO)
                .build();

        Matricula guardada = matriculaRepository.save(matricula);

        // 6. Llamada automática a ms-pagos para generar matrícula + 10 mensualidades
        try {
            pagoFeignClient.generarConceptosIniciales(guardada.getId(), guardada.getAnioEscolar());
        } catch (Exception e) {
            // No rompemos la matrícula si ms-pagos está caído
            System.out.println("No se pudo generar conceptos de pago para la matrícula "
                    + guardada.getId() + ": " + e.getMessage());
        }

        // 7. Actualizar grado/sección del alumno en ms-auth
        try {
            if (request.getGrado() != null && request.getSeccion() != null) {
                alumnoFeignClient.actualizarGradoSeccion(
                        alumnoDto.getId(),
                        request.getGrado(),
                        request.getSeccion()
                );
            }
        } catch (Exception e) {
            System.out.println("No se pudo actualizar grado/sección del alumno "
                    + alumnoDto.getId() + ": " + e.getMessage());
        }

        // 8. Volver a leer el alumno para que ya venga con el grado/sección actualizados
        AlumnoResponseDto alumnoActualizado = obtenerAlumno(alumnoDto.getId());

        // Por ahora no tenemos apoderado asociado directo aquí -> null
        return mapToResponse(guardada, alumnoActualizado, null);
    }

    @Override
    public MatriculaResponseDto obtenerPorId(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada"));

        AlumnoResponseDto alumnoDto = obtenerAlumno(matricula.getAlumnoId());
        ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(matricula.getAlumnoId());

        return mapToResponse(matricula, alumnoDto, apoderado);
    }


    @Override
    public List<MatriculaResponseDto> listarTodas() {
        return matriculaRepository.findAll()
                .stream()
                .map(m -> {
                    AlumnoResponseDto alumno = obtenerAlumno(m.getAlumnoId());
                    ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(m.getAlumnoId());
                    return mapToResponse(m, alumno, apoderado);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<MatriculaResponseDto> listarPorAlumno(Integer alumnoId) {
        return matriculaRepository.findByAlumnoId(alumnoId)
                .stream()
                .map(m -> {
                    AlumnoResponseDto alumno = obtenerAlumno(m.getAlumnoId());
                    ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(m.getAlumnoId());
                    return mapToResponse(m, alumno, apoderado);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<MatriculaResponseDto> listarPorAnio(Integer anioEscolar) {
        return matriculaRepository.findByAnioEscolar(anioEscolar)
                .stream()
                .map(m -> {
                    AlumnoResponseDto alumno = obtenerAlumno(m.getAlumnoId());
                    ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(m.getAlumnoId());
                    return mapToResponse(m, alumno, apoderado);
                })
                .collect(Collectors.toList());
    }


    @Override
    public MatriculaResponseDto cambiarEstado(Long id, EstadoMatricula nuevoEstado) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada"));

        matricula.setEstado(nuevoEstado);
        Matricula actualizada = matriculaRepository.save(matricula);

        AlumnoResponseDto alumnoDto = obtenerAlumno(actualizada.getAlumnoId());
        ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(actualizada.getAlumnoId());

        return mapToResponse(actualizada, alumnoDto, apoderado);
    }


    @Override
    public MatriculaResponseDto obtenerPorCodigo(String codigoMatricula) {
        Matricula matricula = matriculaRepository.findByCodigoMatricula(codigoMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada para código: " + codigoMatricula));

        AlumnoResponseDto alumnoDto = obtenerAlumno(matricula.getAlumnoId());
        ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(matricula.getAlumnoId());

        return mapToResponse(matricula, alumnoDto, apoderado);
    }


    @Override
    public List<MatriculaResponseDto> listarPorDniAlumno(String dni) {
        ResponseEntity<AlumnoResponseDto> resp = alumnoFeignClient.buscarAlumnoPorDni(dni);
        AlumnoResponseDto alumno = resp.getBody();

        if (alumno == null || alumno.getId() == null) {
            throw new RuntimeException("No se encontró alumno para DNI: " + dni);
        }

        ApoderadoResponseDto apoderado = obtenerApoderadoPrincipal(alumno.getId());

        return matriculaRepository.findByAlumnoId(alumno.getId())
                .stream()
                .map(m -> mapToResponse(m, alumno, apoderado))
                .collect(Collectors.toList());
    }


    @Override
    public List<MatriculaResponseDto> listarPorDniApoderado(String dni) {
        ResponseEntity<ApoderadoResponseDto> resp = apoderadoFeignClient.buscarApoderadoPorDni(dni);
        ApoderadoResponseDto apoderado = resp.getBody();

        if (apoderado == null || apoderado.getAlumnoId() == null) {
            throw new RuntimeException("No se encontró apoderado o alumno para DNI: " + dni);
        }

        AlumnoResponseDto alumno = obtenerAlumno(apoderado.getAlumnoId());

        return matriculaRepository.findByAlumnoId(apoderado.getAlumnoId())
                .stream()
                .map(m -> mapToResponse(m, alumno, apoderado)) // 👈 AQUÍ VA EL APODERADO
                .collect(Collectors.toList());
    }




    // ================== Helpers ==================
    /** Devuelve el primer apoderado del alumno (o null si no hay) */
    private ApoderadoResponseDto obtenerApoderadoPrincipal(Integer alumnoId) {
        if (alumnoId == null) return null;

        try {
            var resp = apoderadoFeignClient.listarPorAlumno(alumnoId);
            List<ApoderadoResponseDto> lista = resp.getBody();
            if (resp.getStatusCode().is2xxSuccessful()
                    && lista != null
                    && !lista.isEmpty()) {
                return lista.get(0); // tomamos el primero
            }
        } catch (Exception e) {
            System.out.println("No se pudo obtener apoderado para alumno " + alumnoId
                    + ": " + e.getMessage());
        }
        return null;
    }
    private String generarCodigoMatricula(Integer anioEscolar) {
        Long count = matriculaRepository.countByAnioEscolar(anioEscolar);
        long correlativo = (count == null ? 0 : count) + 1;

        String numero = String.format("%04d", correlativo); // 0001, 0002...
        return "MAT-" + anioEscolar + "-" + numero;
    }

    private AlumnoResponseDto obtenerAlumno(Integer alumnoId) {
        if (alumnoId == null) {
            return null;
        }

        try {
            ResponseEntity<AlumnoResponseDto> resp = alumnoFeignClient.buscarAlumnoPorId(alumnoId);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                return resp.getBody();
            }
        } catch (Exception e) {
            System.out.println("No se pudo obtener alumno desde ms-auth para id "
                    + alumnoId + ": " + e.getMessage());
        }

        // Si falla el feign o ms-auth está caído, devolvemos algo mínimo
        AlumnoResponseDto dto = new AlumnoResponseDto();
        dto.setId(alumnoId);
        dto.setNombres("N/D");
        dto.setApellidos("");
        dto.setActivo(false);
        return dto;
    }

    // Versión principal con apoderado opcional
    private MatriculaResponseDto mapToResponse(
            Matricula m,
            AlumnoResponseDto alumno,
            ApoderadoResponseDto apoderado // puede ser null
    ) {
        // Alumno resumido
        AlumnoResumenDto alumnoResumen = AlumnoResumenDto.builder()
                .id(alumno.getId())
                .nombres(alumno.getNombres())
                .apellidos(alumno.getApellidos())
                .dni(alumno.getDni())
                .gradoActual(alumno.getGradoActual())
                .seccionActual(alumno.getSeccionActual())
                .activo(alumno.getActivo())
                .build();

        // Apoderado resumido (puede no venir)
        ApoderadoResumenDto apoderadoResumen = null;
        if (apoderado != null && apoderado.getId() != null) {
            apoderadoResumen = ApoderadoResumenDto.builder()
                    .id(apoderado.getId())
                    .nombres(apoderado.getNombres())
                    .apellidos(apoderado.getApellidos())
                    .dni(apoderado.getDni())
                    .telefono(apoderado.getTelefono())
                    .parentesco(apoderado.getParentesco())
                    .build();
        }

        return MatriculaResponseDto.builder()
                .id(m.getId())
                .codigoMatricula(m.getCodigoMatricula())
                .anioEscolar(m.getAnioEscolar())
                .tipoMatricula(m.getTipoMatricula())
                .grado(m.getGrado())
                .seccion(m.getSeccion())
                .fechaMatricula(m.getFechaMatricula())
                .estado(m.getEstado())
                .alumno(alumnoResumen)
                .apoderado(apoderadoResumen) // 👈 aquí lo adjuntamos
                .build();
    }

    // Versión rápida cuando no tenemos apoderado
    private MatriculaResponseDto mapToResponse(Matricula m, AlumnoResponseDto alumno) {
        return mapToResponse(m, alumno, null);
    }


}
