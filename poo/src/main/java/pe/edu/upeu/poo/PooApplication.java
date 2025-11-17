package pe.edu.upeu.poo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.edu.upeu.poo.clases.Moto;
import pe.edu.upeu.poo.clases.Vehiculo;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PooApplication {

    public static void main(String[] args) {
        Vehiculo automovil = new Vehiculo();
        automovil.setMarca("Toyota");

        automovil.getColor("Negro");
        System.out.println("acelerado: " + automovil.getAcelerado());
        System.out.println("color: " + automovil.setColor(2));
        System.out.println("modelo: " + automovil.getModelo());
        System.out.println("automovil: " + automovil.toString());
        automovil.encendido();
        System.out.println("automovil: " + automovil.toString());

        System.out.println("automovil: " + automovil.toString());
        System.out.println("acelerado======================: " + automovil.getAcelerado());
        automovil.setAcelerado(2);
        System.out.println("Toyota: " + automovil.toString());
        Vehiculo automovil2 = new Vehiculo();
        automovil2.setMarca("Renault");
        System.out.println("Renault: " + automovil2.toString());

        List<Vehiculo> automovils = new ArrayList<>();
        automovils.add(automovil);
        automovils.add(automovil2);
        System.out.println("Autmoviles: " + automovils.toString());

        for (Vehiculo item : automovils) {
            item.setEstado(false);
        }
        System.out.println("Autmoviles despues del apagado: " + automovils.toString());

        SpringApplication.run(PooApplication.class, args);

        Moto moto = new Moto();
        moto.setMarca("Toyota");
        moto.encendido();
        moto.ruedas = 2;

    }

}
