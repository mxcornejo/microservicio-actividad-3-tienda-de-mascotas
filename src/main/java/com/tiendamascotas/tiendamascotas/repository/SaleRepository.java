package com.tiendamascotas.tiendamascotas.repository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tiendamascotas.tiendamascotas.dto.ProfitSummary;
import com.tiendamascotas.tiendamascotas.models.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    // Ya los tienes (útiles para otras cosas / servicio)
    List<Sale> findByDate(LocalDate date);

    List<Sale> findByDateBetween(LocalDate start, LocalDate end);

    // --- Resumen diario
    @Query("""
                select new com.tiendamascotas.tiendamascotas.dto.ProfitSummary(
                    cast(:date as string),
                    :currency,
                    coalesce(sum(s.quantity * s.unitPrice), 0.0),
                    coalesce(sum(p.cost * s.quantity), 0.0),
                    cast(count(s) as integer)
                )
                from Sale s
                join Product p on p.id = s.productId
                where s.date = :date
            """)
    ProfitSummary resumenPorDia(@Param("date") LocalDate date,
            @Param("currency") String currency);

    // --- Resumen mensual
    @Query("""
                select new com.tiendamascotas.tiendamascotas.dto.ProfitSummary(
                    concat(concat(cast(:year as string), '-'), cast(:month as string)),
                    :currency,
                    coalesce(sum(s.quantity * s.unitPrice), 0.0),
                    coalesce(sum(p.cost * s.quantity), 0.0),
                    cast(count(s) as integer)
                )
                from Sale s
                join Product p on p.id = s.productId
                where year(s.date) = :year and month(s.date) = :month
            """)
    ProfitSummary resumenPorMes(@Param("year") int year,
            @Param("month") int month,
            @Param("currency") String currency);

    // --- Resumen anual
    @Query("""
                select new com.tiendamascotas.tiendamascotas.dto.ProfitSummary(
                    cast(:year as string),
                    :currency,
                    coalesce(sum(s.quantity * s.unitPrice), 0.0),
                    coalesce(sum(p.cost * s.quantity), 0.0),
                    cast(count(s) as integer)
                )
                from Sale s
                join Product p on p.id = s.productId
                where year(s.date) = :year
            """)
    ProfitSummary resumenPorAnio(@Param("year") int year,
            @Param("currency") String currency);

    // --- Unidades vendidas por categoría (crudo)
    @Query("""
                select p.category as category, sum(s.quantity) as units
                from Sale s
                join Product p on p.id = s.productId
                where s.date between :from and :to
                group by p.category
                order by p.category
            """)
    List<Object[]> rawCategoryUnits(@Param("from") LocalDate from,
            @Param("to") LocalDate to);

    // --- Adaptador para devolver Map<String, Integer> tal cual lo espera tu
    // Controller
    default Map<String, Integer> salesUnitsByCategory(LocalDate from, LocalDate to) {
        Map<String, Integer> out = new LinkedHashMap<>();
        for (Object[] row : rawCategoryUnits(from, to)) {
            String category = (String) row[0];
            Integer units = ((Number) row[1]).intValue();
            out.put(category, units);
        }
        return out;
    }
}
