package pe.edu.upeu.msauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.msauth.dto.*;
import pe.edu.upeu.msauth.service.AlumnoService;
import pe.edu.upeu.msauth.service.ApoderadoService;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private ApoderadoService apoderadoService;

    /**
     * Registra un Alumno y su Apoderado en una sola llamada.
     */
    @PostMapping("/alumno-apoderado")
    public ResponseEntity<RegistroAlumnoApoderadoResponseDto> registrarAlumnoYApoderado(
            @RequestBody RegistroAlumnoApoderadoRequestDto dto
    ) {
        AlumnoRequestDto alumnoReq = dto.getAlumno();
        if (alumnoReq == null) {
            throw new RuntimeException("Datos de alumno son obligatorios");
        }

        AlumnoResponseDto alumnoResp = alumnoService.crearAlumno(alumnoReq);

        ApoderadoRequestDto apoderadoReq = dto.getApoderado();
        if (apoderadoReq == null) {
            throw new RuntimeException("Datos de apoderado son obligatorios");
        }

        // Forzamos el alumnoId a ser el creado
        apoderadoReq.setAlumnoId(alumnoResp.getId());
        System.out.println(">>> alumno creado con id = " + alumnoResp.getId());
        System.out.println(">>> apoderadoReq.alumnoId = " + apoderadoReq.getAlumnoId());

        ApoderadoResponseDto apoderadoResp = apoderadoService.crearApoderado(apoderadoReq);

        RegistroAlumnoApoderadoResponseDto response = RegistroAlumnoApoderadoResponseDto.builder()
                .alumno(alumnoResp)
                .apoderado(apoderadoResp)
                .build();

        return ResponseEntity.ok(response);
    }
}
