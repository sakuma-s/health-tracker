package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.dto.WeeklyAverage;
import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Override
    public List<HealthRecord> findByUserOrderByDateDesc(User user) {
        return healthRecordRepository.findByUserOrderByDateDesc(user);
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
                            .mapToDouble(HealthRecord::getSleepHours)
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
}