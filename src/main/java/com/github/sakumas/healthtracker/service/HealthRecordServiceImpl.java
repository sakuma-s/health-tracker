package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.dto.MonthlyAverage;
import com.github.sakumas.healthtracker.dto.WeeklyAverage;
import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Override
    public List<HealthRecord> findByUserOrderByDateDescIdDesc(User user) {
        return healthRecordRepository.findByUserOrderByDateDescIdDesc(user);
    }

    @Override
    public HealthRecord findById(Long id) {
        return healthRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("レコードが見つかりません"));
    }

    @Override
    public void save(HealthRecord healthRecord) {
        healthRecordRepository.save(healthRecord);
    }

    @Override
    public void deleteById(Long id) {
        healthRecordRepository.deleteById(id);
    }

    @Override
    public List<WeeklyAverage> getWeeklyAverages(User user) {
        List<HealthRecord> records = healthRecordRepository.findByUserOrderByDateAsc(user);
        List<WeeklyAverage> weeklyAverages = new ArrayList<>();

        if (records.isEmpty()) {
            return weeklyAverages;
        }

        // 最初のレコードの週の月曜日を起点にする
        LocalDate firstDate = records.get(0).getDate();
        LocalDate weekStart = firstDate.with(DayOfWeek.MONDAY);

        while (true) {
            LocalDate weekEnd = weekStart.plusDays(6);
            final LocalDate start = weekStart;
            final LocalDate end = weekEnd;

            List<HealthRecord> weekRecords = records.stream()
                    .filter(r -> !r.getDate().isBefore(start) && !r.getDate().isAfter(end))
                    .collect(Collectors.toList());

            if (weekRecords.isEmpty()) {
                // データのある週を超えたら終了
                if (weekStart.isAfter(records.get(records.size() - 1).getDate())) {
                    break;
                }
            } else {
                // 週が完結していない場合はスキップ
                if (weekEnd.isBefore(LocalDate.now())) {
                    double avgSleep = weekRecords.stream()
                            .filter(healthRecord -> healthRecord.getSleepMinutes() != null)
                            .mapToInt(HealthRecord::getSleepMinutes)
                            .average()
                            .orElse(0);
                    double avgFatigue = weekRecords.stream()
                            .mapToInt(HealthRecord::getFatigueLevel)
                            .average()
                            .orElse(0);

                    weeklyAverages.add(new WeeklyAverage(weekStart, weekEnd, avgSleep, avgFatigue));
                }
            }
            weekStart = weekStart.plusWeeks(1);
        }
        return weeklyAverages;
    }
    @Override
    public List<MonthlyAverage> getMonthlyAverages(User user) {
        List<HealthRecord> records = healthRecordRepository.findByUserOrderByDateAsc(user);

        Map<YearMonth, List<HealthRecord>> grouped = records.stream()
                .collect(Collectors.groupingBy(r -> YearMonth.from(r.getDate())));

        List<MonthlyAverage> monthlyAverages = new ArrayList<>();
        for (Map.Entry<YearMonth, List<HealthRecord>> entry : grouped.entrySet()) {
        YearMonth yearMonth = entry.getKey();
        List<HealthRecord> monthRecords = entry.getValue();

        double avgSleep = monthRecords.stream()
                .filter(healthRecord -> healthRecord.getSleepMinutes() != null)
                .mapToDouble(HealthRecord::getSleepMinutes)
                .average()
                .orElse(0);
        double avgFatigue = monthRecords.stream()
                .mapToInt(HealthRecord::getFatigueLevel)
                .average()
                .orElse(0);

        monthlyAverages.add(new MonthlyAverage(yearMonth.getYear(), yearMonth.getMonthValue(), avgSleep, avgFatigue));

        }
        monthlyAverages.sort(Comparator.comparing(m -> m.getYear() * 100 + m.getMonth()));

        return monthlyAverages;
    }
    @Override
    public List<HealthRecord> searchByMemo(User user, String keyword) {
        return healthRecordRepository.findByUserAndMemoContainingOrderByDateDesc(user, keyword);
    }
}