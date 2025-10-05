package com.tiendamascotas.tiendamascotas.controllers;

import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tiendamascotas.tiendamascotas.models.Customer;
import com.tiendamascotas.tiendamascotas.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public CollectionModel<EntityModel<Customer>> getAllCustomers() {
        List<Customer> customers = repo.findAll();
        var items = customers.stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CustomerController.class).getCustomerById(c.getId())).withSelfRel()))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Customer> getCustomerById(@PathVariable int id) {
        var c = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        return EntityModel.of(c, linkTo(methodOn(CustomerController.class).getCustomerById(id)).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
    }

}
