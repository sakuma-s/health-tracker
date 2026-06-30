package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;

import java.util.List;

public interface HealthRecordService {

    List<HealthRecord> findByUser(User user);

    HealthRecord findById(Long id);

    void save(HealthRecord healthRecord);

    void deleteById(Long id);

}
