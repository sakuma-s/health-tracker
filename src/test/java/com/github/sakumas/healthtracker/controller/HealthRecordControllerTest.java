package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.repository.UserRepository;
import com.github.sakumas.healthtracker.service.HealthRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
