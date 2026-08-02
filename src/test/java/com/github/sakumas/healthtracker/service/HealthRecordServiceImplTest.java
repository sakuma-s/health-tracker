package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.dto.WeeklyAverage;
import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.HealthRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthRecordServiceImplTest {

    @Mock
    private HealthRecordRepository healthRecordRepository;

    @InjectMocks
    private HealthRecordServiceImpl healthRecordService;

    @Test
    void 週平均が正しく計算される() {
        // 準備
        User user = new User();
        HealthRecord mondayRecord = new HealthRecord();
        mondayRecord.setDate(LocalDate.of(2026, 7, 6));
        mondayRecord.setSleepMinutes(360);
        mondayRecord.setFatigueLevel(2);

        HealthRecord wendnesdayRecord = new HealthRecord();
        wendnesdayRecord.setDate(LocalDate.of(2026, 7, 8));
        wendnesdayRecord.setSleepMinutes(480);
        wendnesdayRecord.setFatigueLevel(4);

        when(healthRecordRepository.findByUserOrderByDateAsc(user))
                .thenReturn(List.of(mondayRecord, wendnesdayRecord));

        // 実行
        List<WeeklyAverage> result = healthRecordService.getWeeklyAverages(user);

        // 確認
        assertThat(result).hasSize(1); // 表示される
    }
        @Test
        void レコードが空の場合は空リストを返す () {
            User emptyUser = new User();
            when(healthRecordRepository.findByUserOrderByDateAsc(emptyUser))
                    .thenReturn(List.of());

            List<WeeklyAverage> emptyResult = healthRecordService.getWeeklyAverages(emptyUser);

            assertThat(emptyResult).isEmpty();
        }
        @Test
        void 今週のデータは表示されない() {
            User todayUser = new User();
            HealthRecord todayRecord = new HealthRecord();
            todayRecord.setDate(LocalDate.now());//今日の日付
            todayRecord.setSleepMinutes(420);
            todayRecord.setFatigueLevel(3);
            when(healthRecordRepository.findByUserOrderByDateAsc(todayUser))
                    .thenReturn(List.of(todayRecord));
            List<WeeklyAverage> todayResult = healthRecordService.getWeeklyAverages(todayUser);

            assertThat(todayResult).isEmpty();
        }
    @Test
    void 複数週のデータがそれぞれ正しく計算される() {
        User multiUser = new User();

        // 1週目：2026/06/29〜2026/07/05
        HealthRecord week1Record1 = new HealthRecord();
        week1Record1.setDate(LocalDate.of(2026, 6, 30));
        week1Record1.setSleepMinutes(480);
        week1Record1.setFatigueLevel(2);

        HealthRecord week1Record2 = new HealthRecord();
        week1Record2.setDate(LocalDate.of(2026, 7, 1));
        week1Record2.setSleepMinutes(480);
        week1Record2.setFatigueLevel(4);

        // 2週目：2026/07/06〜2026/07/12
        HealthRecord week2Record1 = new HealthRecord();
        week2Record1.setDate(LocalDate.of(2026, 7, 7));
        week2Record1.setSleepMinutes(420);
        week2Record1.setFatigueLevel(3);

        when(healthRecordRepository.findByUserOrderByDateAsc(multiUser))
                .thenReturn(List.of(week1Record1, week1Record2, week2Record1));

        List<WeeklyAverage> multiResult = healthRecordService.getWeeklyAverages(multiUser);

        assertThat(multiResult).hasSize(2);
        assertThat(multiResult.get(0).getAvgSleepMinutes()).isEqualTo(420.0); // (360+480)/2
        assertThat(multiResult.get(0).getAvgFatigueLevel()).isEqualTo(3.0); // (2+4)/2
        assertThat(multiResult.get(1).getAvgSleepMinutes()).isCloseTo(375.5, offset(0.01));
        assertThat(multiResult.get(1).getAvgFatigueLevel()).isEqualTo(3.0);
    }
    }