package com.tiendamascotas.tiendamascotas.models;

public class Product {
    private int id;
    private String name;
    private String category;
    private double cost;
    private double price;

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
