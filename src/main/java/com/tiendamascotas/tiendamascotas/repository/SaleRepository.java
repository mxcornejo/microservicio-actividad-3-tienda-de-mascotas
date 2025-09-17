package com.tiendamascotas.tiendamascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendamascotas.tiendamascotas.models.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

}