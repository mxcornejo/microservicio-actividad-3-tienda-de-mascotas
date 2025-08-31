package com.tiendamascotas.tiendamascotas.dto;

public class ProfitSummary {
    private String period;
    private String currency;
    private double revenue;
    private double cost;
    private int saleCount;

    public ProfitSummary(String period, String currency, double revenue, double cost, int saleCount) {
        this.period = period;
        this.currency = currency;
        this.revenue = revenue;
        this.cost = cost;
        this.saleCount = saleCount;
    }

    public String getPeriod() {
        return period;
    }

    public String getCurrency() {
        return currency;
    }

    public double getRevenue() {
        return revenue;
    }

    public double getCost() {
        return cost;
    }

    public int getSaleCount() {
        return saleCount;
    }

}
