package config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import engine.Engine;

public class RemoteSearchConfig implements SearchConfig {

    private long ttl;
    private TimeUnit unit;
    private short amount;
    private List<Engine> engines;

    public RemoteSearchConfig(long ttl, TimeUnit unit, short amount, List<Engine> engines) {
        this.ttl = ttl;
        this.unit = unit;
        this.amount = amount;
        this.engines = engines;
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
        return engines;
    }

    public void setMockEngines(List<Engine> engines) {
        this.engines = engines;
    }
}
