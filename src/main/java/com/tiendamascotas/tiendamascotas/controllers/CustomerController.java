package com.tiendamascotas.tiendamascotas.controllers;

import java.util.List;
import java.util.Optional;

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
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable int id) {
        var c = repo.findById(id);
        if (c == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        return c;
    }

}
