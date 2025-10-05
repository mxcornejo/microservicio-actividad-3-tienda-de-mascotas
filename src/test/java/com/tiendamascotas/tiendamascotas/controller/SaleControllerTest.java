package com.tiendamascotas.tiendamascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendamascotas.tiendamascotas.controllers.SaleController;
import com.tiendamascotas.tiendamascotas.models.Sale;
import com.tiendamascotas.tiendamascotas.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SaleController.class)
public class SaleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SaleRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void all_returnsSales() throws Exception {
        var s1 = new Sale(1, 1, 1, 2, LocalDate.of(2024, 1, 1), 12990.0);
        var s2 = new Sale(2, 2, 1, 1, LocalDate.of(2024, 1, 2), 5990.0);
        Mockito.when(repo.findAll()).thenReturn(List.of(s1, s2));

        mvc.perform(get("/api/sales")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("12990.0")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("_links")));
    }
}
