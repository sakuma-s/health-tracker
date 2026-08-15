package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.entity.HealthRecord;
import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.UserRepository;
import com.github.sakumas.healthtracker.service.HealthRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthRecordController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class HealthRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HealthRecordService healthRecordService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void 新規登録フォームを開いたとき200が返ること() throws Exception {
        mockMvc.perform(get("/records/new"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 保存してほしい中身の検証() throws Exception {
        User user = new User();
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/records")
                        .param("date", "2026-08-14")
                        .param("sleepHoursInput", "7")
                        .param("fatigueLevel", "1")
                        .param("memo", "検証")
                        .with(csrf()))
                        .andExpect(status().is3xxRedirection());

        ArgumentCaptor<HealthRecord> captor = ArgumentCaptor.forClass(HealthRecord.class);
        verify(healthRecordService).save(captor.capture());
        HealthRecord savedRecord = captor.getValue();

        assertThat(savedRecord.getSleepMinutes()).isEqualTo(420);

    }

    @Test
    @WithMockUser
    void 時間分の未入力パターンの検証() throws Exception {
        User user = new User();
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/records")
                .param("date", "2026-08-15")
                .param("fatigueLevel", "2")
                .param("memo", "時間分未入力")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<HealthRecord> captor = ArgumentCaptor.forClass(HealthRecord.class);
        verify(healthRecordService).save(captor.capture());
        HealthRecord savedRecord = captor.getValue();
        assertThat(savedRecord.getSleepMinutes()).isNull();
    }


}
