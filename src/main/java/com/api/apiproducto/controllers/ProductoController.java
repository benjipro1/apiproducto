package com.api.apiproducto.controllers;

import com.api.apiproducto.dto.ProductoDTO;
import com.api.apiproducto.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService service;

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id, @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hateoas/{id}")
    public ProductoDTO obtenerHateoas(@PathVariable Integer id) {
        ProductoDTO dto = service.obtenerPorId(id);
        dto.add(linkTo(methodOn(ProductoController.class).obtenerHateoas(id)).withSelfRel());
        dto.add(linkTo(methodOn(ProductoController.class).listarHateoas()).withRel("TODOS"));
        dto.add(linkTo(methodOn(ProductoController.class).eliminar(id)).withRel("ELIMINAR"));
        return dto;
    }
    
    @GetMapping("/hateoas")
    public List<ProductoDTO> listarHateoas() {
        List<ProductoDTO> productos = service.listar();
        for (ProductoDTO dto : productos) {
            dto.add(linkTo(methodOn(ProductoController.class).obtenerHateoas(dto.getId())).withSelfRel());
        }
        return productos;
    }

}