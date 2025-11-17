package com.example.mscatalogo.Controller;

import com.example.mscatalogo.Entity.Categoria;
import com.example.mscatalogo.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> list = categoriaService.listar();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        Optional<Categoria> opt = categoriaService.buscarPorId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categoria> guardar(@RequestBody Categoria categoria) {
        Categoria saved = categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Optional<Categoria> existing = categoriaService.buscarPorId(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // asegurar que el id del entity se sincronice con la ruta
        categoria.setId(id);
        Categoria updated = categoriaService.actualizar(categoria);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Optional<Categoria> existing = categoriaService.buscarPorId(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.borrarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
