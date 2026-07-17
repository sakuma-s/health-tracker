package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.dto.WeeklyAverage;
import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.UserRepository;
import com.github.sakumas.healthtracker.service.HealthRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/records")
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;
    @Autowired
    private UserRepository userService;

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("healthRecord" , new HealthRecord());
        return "records/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute HealthRecord healthRecord, BindingResult result) {
        if (result.hasErrors()) {
            return "records/form";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        healthRecord.setUser(user);
        healthRecordService.save(healthRecord);
        return "redirect:/records";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        HealthRecord healthRecord = healthRecordService.findById(id);
        model.addAttribute("healthRecord", healthRecord);
        return "records/form";
    }
    @GetMapping
    public String list(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        model.addAttribute("healthRecords", healthRecordService.findByUserOrderByDateDesc(user));

        List<WeeklyAverage> weeklyAverages = healthRecordService.getWeeklyAverages(user);
        model.addAttribute("weeklyAverages", weeklyAverages);

        // Chart.js用データ
        StringBuilder labels = new StringBuilder("[");
        StringBuilder sleep = new StringBuilder("[");
        StringBuilder fatigue = new StringBuilder("[");

        for (int i = 0; i < weeklyAverages.size(); i++) {
            WeeklyAverage w = weeklyAverages.get(i);
            if (i > 0) {
                labels.append(",");
                sleep.append(",");
                fatigue.append(",");
            }
            labels.append("\"").append(w.getStartDate()).append("〜").append(w.getEndDate()).append("\"");
            sleep.append(w.getAvgSleepHours());
            fatigue.append(w.getAvgFatigueLevel());
        }
        labels.append("]");
        sleep.append("]");
        fatigue.append("]");
        model.addAttribute("weeklyLabels", labels.toString());
        model.addAttribute("weeklySleep", sleep.toString());
        model.addAttribute("weeklyFatigue", fatigue.toString());

        return "records/list";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,@Valid @ModelAttribute HealthRecord healthRecord, BindingResult result) {
        if (result.hasErrors()) {
            return "records/form";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        healthRecord.setId(id);
        healthRecord.setUser(user);
        healthRecordService.save(healthRecord);
        return "redirect:/records";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        healthRecordService.deleteById(id);
        return "redirect:/records";
    }
}
