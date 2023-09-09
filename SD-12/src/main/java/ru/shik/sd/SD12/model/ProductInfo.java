package ru.shik.sd.SD12.model;

import ru.shik.sd.SD12.currency.Currency;

public class ProductInfo {

    private String name;
    private double price;
    private Currency currency;

    public ProductInfo() {
    }

    public ProductInfo(String name, double price, Currency currency) {
        this.name = name;
        this.price = price;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
