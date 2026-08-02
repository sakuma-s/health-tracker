package com.github.sakumas.healthtracker.dto;

import java.time.LocalDate;

public class WeeklyAverage {

    private LocalDate startDate;
    private LocalDate endDate;
    private double avgSleepMinutes;
    private double avgFatigueLevel;

    public WeeklyAverage(LocalDate startDate, LocalDate endDate, double avgSleepMinutes, double avgFatigueLevel) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.avgSleepMinutes = avgSleepMinutes;
        this.avgFatigueLevel = avgFatigueLevel;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getAvgSleepMinutes() { return avgSleepMinutes; }
    public double getAvgFatigueLevel() { return avgFatigueLevel; }
}
