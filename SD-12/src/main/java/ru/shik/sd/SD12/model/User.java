package ru.shik.sd.SD12.model;

import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Table("users")
public class User implements Persistable<Long> {

    @Id
    private long id;
    private String name;
    private String currency;

    public User() {
    }

    public User(long id, String name, String currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
