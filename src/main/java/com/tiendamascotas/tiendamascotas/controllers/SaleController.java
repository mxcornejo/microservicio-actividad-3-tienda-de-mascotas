package com.tiendamascotas.tiendamascotas.controllers;

import com.tiendamascotas.tiendamascotas.dto.ProfitSummary;
import com.tiendamascotas.tiendamascotas.models.Sale;
import com.tiendamascotas.tiendamascotas.repository.SaleRepository;

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

    private final SaleRepository repo;

    public SaleController(SaleRepository repo) {
        this.repo = repo;
    }

    // --- Listado de ventas
    @GetMapping
    public List<Sale> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Sale byId(@PathVariable @Positive int id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + id));
    }

    // --- Filtros
    @GetMapping("/by-date")
    public List<Sale> byDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repo.findByDate(date);
    }

    @GetMapping("/by-range")
    public List<Sale> byRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return repo.findByDateBetween(start, end);
    }

    // --- Ganancias (diaria, mensual, anual) como resumen de utilidades
    @GetMapping("/profit/daily")
    public ProfitSummary daily(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repo.resumenPorDia(date, "CLP");
    }

    @GetMapping("/profit/monthly")
    public ProfitSummary monthly(@RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1) @Max(12) int month) {
        return repo.resumenPorMes(year, month, "CLP");
    }

    @GetMapping("/profit/annual")
    public ProfitSummary annual(@RequestParam @Min(2000) @Max(2100) int year) {
        return repo.resumenPorAnio(year, "CLP");
    }

    @GetMapping("/stats/category-count")
    public Map<String, Integer> categoryCount(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from))
            throw new IllegalArgumentException("'to' no puede ser anterior a 'from'");
        return repo.salesUnitsByCategory(from, to);
    }
}
