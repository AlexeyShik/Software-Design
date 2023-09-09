import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class TimeUtils {

    private TimeUtils() {}

    public static LocalDateTime hoursToDateTime(int hours) {
        LocalDate date = LocalDate.EPOCH;
        if (hours >= 24) {
            date = date.plusDays(hours / 24);
        }
        return LocalDateTime.of(
            date,
            LocalTime.of(hours % 24, 0)
        );
    }

    public static int hoursToSeconds(int hours) {
        return (int) hoursToDateTime(hours).atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static LocalDateTime secondsToDateTime(int seconds) {
        return LocalDateTime.of(
            LocalDate.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault()),
            LocalTime.ofSecondOfDay(seconds)
        );
    }
}
