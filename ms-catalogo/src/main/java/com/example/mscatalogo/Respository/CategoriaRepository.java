package com.example.mscatalogo.Respository;

import com.example.mscatalogo.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository  extends JpaRepository<Categoria,Integer> {
}
