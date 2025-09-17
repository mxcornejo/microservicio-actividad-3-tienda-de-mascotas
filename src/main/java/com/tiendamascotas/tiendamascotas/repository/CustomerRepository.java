package com.tiendamascotas.tiendamascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendamascotas.tiendamascotas.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}