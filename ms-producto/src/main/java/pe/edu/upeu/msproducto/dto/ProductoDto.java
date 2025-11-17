package pe.edu.upeu.msproducto.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class ProductoDto {


    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer idCategoria;
    private CatagoriaDto catagoria;

}
