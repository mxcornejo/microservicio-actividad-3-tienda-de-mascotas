package com.tiendamascotas.tiendamascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendamascotas.tiendamascotas.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}