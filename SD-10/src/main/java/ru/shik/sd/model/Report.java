package ru.shik.sd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class Report {

    private Map<LocalDate, Long> dailyVisits;
    private double averageVisits;
    private double averageDuration;

    public Report() {

    }

    public Report(Map<LocalDate, Long> dailyVisits, double averageVisits, double averageDuration) {
        this.dailyVisits = dailyVisits;
        this.averageVisits = averageVisits;
        this.averageDuration = averageDuration;
    }

    public Map<LocalDate, Long> getDailyVisits() {
        return dailyVisits;
    }

    public void setDailyVisits(Map<LocalDate, Long> dailyVisits) {
        this.dailyVisits = dailyVisits;
    }

    public double getAverageVisits() {
        return averageVisits;
    }

    public void setAverageVisits(double averageVisits) {
        this.averageVisits = averageVisits;
    }

    public double getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(double averageDuration) {
        this.averageDuration = averageDuration;
    }

    @Override
    public String toString() {
        StringBuilder report = new StringBuilder("Report{dailyVisits=");
        for (Map.Entry<LocalDate, Long> entry : dailyVisits.entrySet()) {
            report.append("key=")
                .append(entry.getKey().toString())
                .append(",value=")
                .append(entry.getValue())
                .append(",");
        }
        return report.append(";averageVisits=")
            .append(averageVisits)
            .append(";averageDuration=")
            .append(averageDuration)
            .append("}")
            .toString();
    }
}
