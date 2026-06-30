package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/records")
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;

    @GetMapping
    public String list(Model model) {
        // 後でログイン中のユーザーを取得する処理を追加
        return "records/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("healthRecord" , new HealthRecord());
        return "records/form";
    }

    @PostMapping
    public String save(@ModelAttribute HealthRecord healthRecord) {
        healthRecordService.save(healthRecord);
        return "redirect:/records";
    }
}
