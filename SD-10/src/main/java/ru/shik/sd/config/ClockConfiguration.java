package ru.shik.sd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.shik.sd.clock.ClassicClock;
import ru.shik.sd.clock.Clock;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock() {
        return new ClassicClock();
    }
}
