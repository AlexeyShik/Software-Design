package ru.akirakozov.sd.refactoring.dao;

public class ProductEntry {

    private final String name;
    private final long price;

    public ProductEntry(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
