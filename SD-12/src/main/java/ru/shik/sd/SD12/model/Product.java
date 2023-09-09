package ru.shik.sd.SD12.model;

import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Table("products")
public class Product implements Persistable<Long> {

    @Id
    private long id;
    private String name;
    @Column("price")
    private double priceRUB;

    public Product() {
    }

    public Product(long id, String name, double priceRUB) {
        this.id = id;
        this.name = name;
        this.priceRUB = priceRUB;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceRUB() {
        return priceRUB;
    }

    public void setPriceRUB(double priceRUB) {
        this.priceRUB = priceRUB;
    }
}
