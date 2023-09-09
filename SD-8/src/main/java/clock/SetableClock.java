package clock;

import java.time.Instant;

public class SetableClock implements Clock {

    private Instant now;

    public SetableClock() {
        now = Instant.ofEpochMilli(0);
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }
}
