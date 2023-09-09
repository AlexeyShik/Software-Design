package config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import engine.Engine;
import engine.MockEngine;

public class MockSearchConfig implements SearchConfig {

    private long ttl;
    private TimeUnit unit;
    private short amount;
    private List<MockEngine> mockEngines;

    public MockSearchConfig() {
        super();
    }

    public MockSearchConfig(long ttl, TimeUnit unit, short amount, List<MockEngine> engines) {
        this.ttl = ttl;
        this.unit = unit;
        this.amount = amount;
        this.mockEngines = engines;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public short getAmount() {
        return amount;
    }

    public void setAmount(short amount) {
        this.amount = amount;
    }

    public List<Engine> getEngines() {
        List<Engine> engines = new ArrayList<>(this.mockEngines.size());
        engines.addAll(this.mockEngines);
        return engines;
    }

    public void setMockEngines(List<MockEngine> engines) {
        this.mockEngines = engines;
    }

}
