package com.tiendamascotas.tiendamascotas.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tiendamascotas.tiendamascotas.models.Customer;
import com.tiendamascotas.tiendamascotas.services.DataService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final DataService data;

    public CustomerController(DataService data) {
        this.data = data;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return data.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable int id) {
    var c = data.getCustomerById(id);
    if (c == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    return c;
    }

}
