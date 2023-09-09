package ru.shik.sd.clock;

import java.time.LocalDateTime;

public class SettableClock implements Clock {

    private LocalDateTime currentTime;

    public SettableClock(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public LocalDateTime now() {
        return currentTime;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }
}
