package ru.shik.sd.model;

public class Stock {

    private int id;
    private String name;
    private String companyName;
    private long amount;
    private double price;

    public Stock() {
    }

    public Stock(int id, String name, String companyName, long amount, double price) {
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
