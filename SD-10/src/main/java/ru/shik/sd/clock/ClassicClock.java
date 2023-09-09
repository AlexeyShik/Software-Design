package ru.shik.sd.clock;

import java.time.LocalDateTime;

public class ClassicClock implements Clock {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
