package ru.shik.sd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("price")
    private double price;

    public StockDTO() {
    }

    public StockDTO(int id, String name, String companyName, long amount, double price) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
        this.amount = amount;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
