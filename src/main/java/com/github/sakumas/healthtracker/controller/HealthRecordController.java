package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.dto.MonthlyAverage;
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
        model.addAttribute("actionUrl", "/records"); //追加
        return "records/form";
    }

    @PostMapping
    public String save(
            @Valid @ModelAttribute HealthRecord healthRecord,
            BindingResult result,
            @RequestParam(required = false) Integer sleepHoursInput,
            @RequestParam(required = false) Integer sleepMinutesPart,
            Model model) {

        if (sleepHoursInput == null || sleepMinutesPart == null) {
            model.addAttribute("sleepError", "睡眠時間を入力してください");
            return "records/form";
        }
        if (result.hasErrors()) {
            return "records/form";
        }
        healthRecord.setSleepMinutes(sleepHoursInput * 60 + sleepMinutesPart);
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
        model.addAttribute("actionUrl", "/records/" + id + "/update");
        return "records/form";
    }
    @GetMapping
    public String list(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        model.addAttribute("healthRecords", healthRecordService.findByUserOrderByDateDescIdDesc(user));

        List<WeeklyAverage> weeklyAverages = healthRecordService.getWeeklyAverages(user);
        model.addAttribute("weeklyAverages", weeklyAverages);

        List<MonthlyAverage> monthlyAverages = healthRecordService.getMonthlyAverages(user);
        model.addAttribute("monthlyAverages", monthlyAverages);

        // Chart.js用データ
        StringBuilder weeklyLabels = new StringBuilder("[");
        StringBuilder weeklySleep = new StringBuilder("[");
        StringBuilder weeklyFatigue = new StringBuilder("[");

        for (int i = 0; i < weeklyAverages.size(); i++) {
            WeeklyAverage weekly = weeklyAverages.get(i);
            if (i > 0) {
                weeklyLabels.append(",");
                weeklySleep.append(",");
                weeklyFatigue.append(",");
            }
            weeklyLabels.append("\"")
                    .append(weekly.getStartDate().getMonthValue()).append("/").append(weekly.getStartDate().getDayOfMonth())
                    .append("〜")
                    .append(weekly.getEndDate().getMonthValue()).append("/").append(weekly.getEndDate().getDayOfMonth())
                    .append("\"");
            weeklySleep.append(weekly.getAvgSleepMinutes() / 60.0);
            weeklyFatigue.append(weekly.getAvgFatigueLevel());
        }
        weeklyLabels.append("]");
        weeklySleep.append("]");
        weeklyFatigue.append("]");
        model.addAttribute("weeklyLabels", weeklyLabels.toString());
        model.addAttribute("weeklySleep", weeklySleep.toString());
        model.addAttribute("weeklyFatigue", weeklyFatigue.toString());

        StringBuilder monthLabels = new StringBuilder("[");
        StringBuilder monthSleep = new StringBuilder("[");
        StringBuilder monthFatigue = new StringBuilder("[");

        for (int i = 0; i < monthlyAverages.size(); i++) {
            MonthlyAverage monthly = monthlyAverages.get(i);
            if (i > 0) {
                monthLabels.append(",");
                monthSleep.append(",");
                monthFatigue.append(",");
            }
            monthLabels.append("\"")
                    .append(monthly.getYear())
                    .append("/")
                    .append(monthly.getMonth())
                    .append("\"");
            monthSleep.append(monthly.getAvgSleepMinutes() / 60.0);
            monthFatigue.append(monthly.getAvgFatigueLevel());
        }
        monthLabels.append("]");
        monthSleep.append("]");
        monthFatigue.append("]");
        model.addAttribute("monthlyLabels", monthLabels.toString());
        model.addAttribute("monthlySleep", monthSleep.toString());
        model.addAttribute("monthlyFatigue", monthFatigue.toString());

        return "records/list";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute HealthRecord healthRecord,
                         BindingResult result,
                         @RequestParam(required = false) Integer sleepHoursInput,
                         @RequestParam(required = false) Integer sleepMinutesPart,
                         Model model) {
        if (sleepHoursInput == null || sleepMinutesPart == null) {
            model.addAttribute("sleepError","睡眠時間を入力してください");
            return "records/form";
        }
        if (result.hasErrors()) {
            return "records/form";
        }
        healthRecord.setSleepMinutes(sleepHoursInput * 60 + sleepMinutesPart);

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
    @GetMapping("/search")
    @ResponseBody
    public List<HealthRecord> search(@RequestParam String keyword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        return healthRecordService.searchByMemo(user, keyword);
    }
}
