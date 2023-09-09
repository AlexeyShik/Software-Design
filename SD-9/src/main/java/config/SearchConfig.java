package config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import engine.Engine;

public interface SearchConfig {

    List<Engine> getEngines();

    long getTtl();

    TimeUnit getUnit();

    short getAmount();

}
