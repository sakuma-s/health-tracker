package com.github.sakumas.healthtracker.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class HealthRecordControllerResolveSleepTimeTest {

    private final HealthRecordController controller = new HealthRecordController();
    @Test
    void 時間のみ入力した場合() {
        //時間のみ入力したら成功する
        //時間の入力
        HealthRecordController.SleepTime sleepResults = controller.resolveSleepTime(6, null);
        //時間のみ入力したら、時間と00分を入力。
        assertThat(sleepResults.hours()).isEqualTo(6);
        assertThat(sleepResults.minutes()).isEqualTo(0);
    }

    @Test
    void 時間と分ともに未入力() {
        HealthRecordController.SleepTime hoursAndMinutesNull = controller.resolveSleepTime(null, null);
        assertThat(hoursAndMinutesNull.hours()).isNull();
        assertThat(hoursAndMinutesNull.minutes()).isNull();
    }

    @Test
    void 時間と分の両方が入力されている() {
        HealthRecordController.SleepTime timeAndMinutesEntered = controller.resolveSleepTime(6,30);
        assertThat(timeAndMinutesEntered.hours()).isEqualTo(6);
        assertThat(timeAndMinutesEntered.minutes()).isEqualTo(30);
    }

}
