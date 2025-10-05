package com.tiendamascotas.tiendamascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendamascotas.tiendamascotas.controllers.CustomerController;
import com.tiendamascotas.tiendamascotas.models.Customer;
import com.tiendamascotas.tiendamascotas.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllCustomers_returnsList() throws Exception {
        var c1 = new Customer(1, "Ana", "ana@mail.com");
        var c2 = new Customer(2, "Luis", "luis@mail.com");
        Mockito.when(repo.findAll()).thenReturn(List.of(c1, c2));

        mvc.perform(get("/api/customers")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Ana")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("_links")));
    }
}
