package com.example.mscatalogo.Service;

import com.example.mscatalogo.Entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> listar();
    Optional<Categoria> buscarPorId(Integer id);
    Categoria guardar(Categoria categoria);
    Categoria actualizar(Categoria categoria);
    void borrarPorId(Integer id);
}
