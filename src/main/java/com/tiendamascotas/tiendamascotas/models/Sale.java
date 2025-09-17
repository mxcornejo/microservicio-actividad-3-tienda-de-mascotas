package com.tiendamascotas.tiendamascotas.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SALES")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "SALE_DATE")
    private LocalDate date;

    @Column(name = "unit_price")
    private double unitPrice;

    public Sale() {
    }

    public Sale(int id, int productId, int customerId, int quantity, LocalDate date, double unitPrice) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.quantity = quantity;
        this.date = date;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

}
