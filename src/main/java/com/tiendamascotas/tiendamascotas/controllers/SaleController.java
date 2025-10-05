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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;
import java.util.List;

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
    public CollectionModel<EntityModel<Sale>> all() {
        var sales = repo.findAll();
        var items = sales.stream()
                .map(s -> EntityModel.of(s, linkTo(methodOn(SaleController.class).byId(s.getId())).withSelfRel()))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(SaleController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Sale> byId(@PathVariable @Positive int id) {
        var sale = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + id));
        return EntityModel.of(sale, linkTo(methodOn(SaleController.class).byId(id)).withSelfRel(),
                linkTo(methodOn(SaleController.class).all()).withRel("sales"));
    }

    // --- Filtros
    @GetMapping("/by-date")
    public CollectionModel<EntityModel<Sale>> byDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var sales = repo.findByDate(date);
        var items = sales.stream()
                .map(s -> EntityModel.of(s, linkTo(methodOn(SaleController.class).byId(s.getId())).withSelfRel()))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(SaleController.class).byDate(date)).withSelfRel());
    }

    @GetMapping("/by-range")
    public CollectionModel<EntityModel<Sale>> byRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        var sales = repo.findByDateBetween(start, end);
        var items = sales.stream()
                .map(s -> EntityModel.of(s, linkTo(methodOn(SaleController.class).byId(s.getId())).withSelfRel()))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(SaleController.class).byRange(start, end)).withSelfRel());
    }

    // --- Ganancias (diaria, mensual, anual) como resumen de utilidades
    @GetMapping("/profit/daily")
    public EntityModel<ProfitSummary> daily(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ProfitSummary summary = repo.resumenPorDia(date, "CLP");
        return EntityModel.of(summary, linkTo(methodOn(SaleController.class).daily(date)).withSelfRel(),
                linkTo(methodOn(SaleController.class).monthly(date.getYear(), date.getMonthValue()))
                        .withRel("monthly"));
    }

    @GetMapping("/profit/monthly")
    public EntityModel<ProfitSummary> monthly(@RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1) @Max(12) int month) {
        ProfitSummary summary = repo.resumenPorMes(year, month, "CLP");
        return EntityModel.of(summary, linkTo(methodOn(SaleController.class).monthly(year, month)).withSelfRel(),
                linkTo(methodOn(SaleController.class).annual(year)).withRel("annual"));
    }

    @GetMapping("/profit/annual")
    public EntityModel<ProfitSummary> annual(@RequestParam @Min(2000) @Max(2100) int year) {
        ProfitSummary summary = repo.resumenPorAnio(year, "CLP");
        return EntityModel.of(summary, linkTo(methodOn(SaleController.class).annual(year)).withSelfRel());
    }

    @GetMapping("/stats/category-count")
    public EntityModel<java.util.Map<String, Integer>> categoryCount(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from))
            throw new IllegalArgumentException("'to' no puede ser anterior a 'from'");
        var map = repo.salesUnitsByCategory(from, to);
        return EntityModel.of(map, linkTo(methodOn(SaleController.class).categoryCount(from, to)).withSelfRel());
    }
}
