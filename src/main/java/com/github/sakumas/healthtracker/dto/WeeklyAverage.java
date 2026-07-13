package com.github.sakumas.healthtracker.dto;

import java.time.LocalDate;

public class WeeklyAverage {

    private LocalDate startDate;
    private LocalDate endDate;
    private double avgSleepHours;
    private double avgFatigueLevel;

    public WeeklyAverage(LocalDate startDate, LocalDate endDate, double avgSleepHours, double avgFatigueLevel) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.avgSleepHours = avgSleepHours;
        this.avgFatigueLevel = avgFatigueLevel;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getAvgSleepHours() { return avgSleepHours; }
    public double getAvgFatigueLevel() { return avgFatigueLevel; }
}
