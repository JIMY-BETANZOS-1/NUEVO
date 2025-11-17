package pe.edu.upeu.poo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.poo.clases.Moto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/moto")
public class MotoController {
    @GetMapping
    public List<Moto> motos() {
        List<Moto> motos = new ArrayList<>();
        Moto moto1 = new Moto();
        moto1.setMarca("Toyota");
        moto1.setRuedas(2);
        motos.add(moto1);
        Moto moto2 = new Moto();
        moto2.setMarca("Juliacaqueño torito");
        moto2.setRuedas(3);
        motos.add(moto2);
        return motos;
    }

}
