package com.tiendamascotas.tiendamascotas.controllers;

import com.tiendamascotas.tiendamascotas.models.Product;
import com.tiendamascotas.tiendamascotas.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }
}
