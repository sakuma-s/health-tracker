package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Override
    public List<HealthRecord> findByUser(User user) {
        return healthRecordRepository.findByUser(user);
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
}
