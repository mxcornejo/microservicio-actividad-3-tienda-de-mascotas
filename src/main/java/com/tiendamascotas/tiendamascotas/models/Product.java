package com.tiendamascotas.tiendamascotas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "category")
    private String category;
    @Column(name = "cost")
    private double cost;
    @Column(name = "price")
    private double price;

    public Product() {
    }

    public Product(int id, String name, String category, double cost, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getCost() {
        return cost;
    }

    public double getPrice() {
        return price;
    }

}
