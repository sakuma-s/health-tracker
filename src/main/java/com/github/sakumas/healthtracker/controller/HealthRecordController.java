package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.UserRepository;
import com.github.sakumas.healthtracker.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/records")
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;
    @Autowired
    private UserRepository userService;

    @GetMapping
    public String list(Model model) {
        // 後でログイン中のユーザーを取得する処理を追加
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        model.addAttribute("healthRecords", healthRecordService.findByUser(user));
        return "records/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("healthRecord" , new HealthRecord());
        return "records/form";
    }

    @PostMapping
    public String save(@ModelAttribute HealthRecord healthRecord) {
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

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute HealthRecord healthRecord) {
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
