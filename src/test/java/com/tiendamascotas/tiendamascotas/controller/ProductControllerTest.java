package com.tiendamascotas.tiendamascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendamascotas.tiendamascotas.controllers.ProductController;
import com.tiendamascotas.tiendamascotas.models.Product;
import com.tiendamascotas.tiendamascotas.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllProducts_returnsList() throws Exception {
        var p1 = new Product(1, "Alimento", "ALIMENTO", 7000.0, 12990.0);
        var p2 = new Product(2, "Collar", "ACCESORIO", 2000.0, 5990.0);
        Mockito.when(repo.findAll()).thenReturn(List.of(p1, p2));

        mvc.perform(get("/api/productos")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Alimento")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("_links")));
    }

    @Test
    void createProduct_returnsCreated() throws Exception {
        var p = new Product(0, "Nuevo", "OTRO", 1000.0, 2500.0);
        var saved = new Product(5, "Nuevo", "OTRO", 1000.0, 2500.0);
        Mockito.when(repo.save(any(Product.class))).thenReturn(saved);

        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nuevo")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("_links")));
    }
}
