package com.github.sakumas.healthtracker.dto;

public class MonthlyAverage {

    private int year;
    private int month;
    private double avgSleepHours;
    private double avgFatigueLevel;

    public MonthlyAverage(int year, int month, double avgSleepHours, double avgFatigueLevel) {
        this.year = year;
        this.month = month;
        this.avgSleepHours = avgSleepHours;
        this.avgFatigueLevel = avgFatigueLevel;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public double getAvgSleepHours() { return avgSleepHours; }
    public double getAvgFatigueLevel() { return avgFatigueLevel; }

}
