package com.tiendamascotas.tiendamascotas.controllers;

import com.tiendamascotas.tiendamascotas.dto.ProfitSummary;
import com.tiendamascotas.tiendamascotas.models.Sale;
import com.tiendamascotas.tiendamascotas.services.DataService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/sales")
public class SaleController {

    private final DataService data;

    public SaleController(DataService data) {
        this.data = data;
    }

    // --- Listado de ventas
    @GetMapping
    public List<Sale> all() {
        return data.obtenerVentas();
    }

    @GetMapping("/{id}")
    public Sale byId(@PathVariable @Positive int id) {
        return data.getSale(id).orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + id));
    }

    // --- Filtros
    @GetMapping("/by-date")
    public List<Sale> byDate(@RequestParam("date")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return data.getSalesByDate(date);
    }

    @GetMapping("/by-range")
    public List<Sale> byRange(@RequestParam("from")
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                              @RequestParam("to")
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from)) throw new IllegalArgumentException("'to' no puede ser anterior a 'from'");
        return data.getSalesByRange(from, to);
    }

    // --- Ganancias (diaria, mensual, anual) como resumen de utilidades
    @GetMapping("/profit/daily")
    public ProfitSummary daily(@RequestParam("date")
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return data.resumenPorDia(date, "CLP");
    }

    @GetMapping("/profit/monthly")
    public ProfitSummary monthly(@RequestParam @Min(2000) @Max(2100) int year,
                                 @RequestParam @Min(1) @Max(12) int month) {
        return data.resumenPorMes(year, month, "CLP");
    }

    @GetMapping("/profit/annual")
    public ProfitSummary annual(@RequestParam @Min(2000) @Max(2100) int year) {
        return data.resumenPorAnio(year, "CLP");
    }

    @GetMapping("/stats/category-count")
    public Map<String, Integer> categoryCount(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from)) throw new IllegalArgumentException("'to' no puede ser anterior a 'from'");
        return data.salesUnitsByCategory(from, to);
    }
}
