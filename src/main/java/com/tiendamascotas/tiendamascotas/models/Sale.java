package com.tiendamascotas.tiendamascotas.models;

import java.time.LocalDate;

public class Sale {
    private int id;
    private int productId;
    private int customerId;
    private int quantity;
    private LocalDate date;
    private double unitPrice;

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
