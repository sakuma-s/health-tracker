package com.github.sakumas.healthtracker.dto;

public class MonthlyAverage {

    private int year;
    private int month;
    private double avgSleepMinutes;
    private double avgFatigueLevel;

    public MonthlyAverage(int year, int month, double avgSleepMinutes, double avgFatigueLevel) {
        this.year = year;
        this.month = month;
        this.avgSleepMinutes = avgSleepMinutes;
        this.avgFatigueLevel = avgFatigueLevel;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public double getAvgSleepMinutes() { return avgSleepMinutes; }
    public double getAvgFatigueLevel() { return avgFatigueLevel; }
}
