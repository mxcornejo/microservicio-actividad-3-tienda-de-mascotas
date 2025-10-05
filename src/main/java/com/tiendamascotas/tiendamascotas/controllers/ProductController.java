package com.tiendamascotas.tiendamascotas.controllers;

import com.tiendamascotas.tiendamascotas.models.Product;
import com.tiendamascotas.tiendamascotas.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public CollectionModel<EntityModel<Product>> getAllProducts() {
        List<Product> products = repo.findAll();
        List<EntityModel<Product>> items = products.stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(ProductController.class).getProductById(p.getId())).withSelfRel()))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Product> getProductById(@PathVariable int id) {
        Product prod = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return EntityModel.of(prod,
                linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Product>> createProduct(@RequestBody Product product) {
        Product saved = repo.save(product);
        EntityModel<Product> model = EntityModel.of(saved,
                linkTo(methodOn(ProductController.class).getProductById(saved.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    public EntityModel<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product updated = repo.findById(id).map(existing -> {
            Product u = new Product(id, product.getName(), product.getCategory(), product.getCost(),
                    product.getPrice());
            return repo.save(u);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return EntityModel.of(updated,
                linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        repo.deleteById(id);
        // return link to collection
        var links = linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products");
        return ResponseEntity.noContent().header("Link", links.getHref()).build();
    }
}
