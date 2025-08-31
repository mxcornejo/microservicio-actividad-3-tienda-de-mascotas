package com.tiendamascotas.tiendamascotas.controllers;

import com.tiendamascotas.tiendamascotas.models.Product;
import com.tiendamascotas.tiendamascotas.services.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final DataService data;

    public ProductController(DataService data) {
        this.data = data;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return data.obtenerProductos();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return data.buscarProducto(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }
}
