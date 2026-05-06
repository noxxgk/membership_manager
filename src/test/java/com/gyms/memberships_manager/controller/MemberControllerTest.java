package com.gyms.memberships_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
import com.gyms.memberships_manager.model.MemberStatus;
import com.gyms.memberships_manager.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    @Test
    void shouldRegisterMemberSuccessfullyAndReturn201() throws Exception {
        MemberRegistrationRequest request = new MemberRegistrationRequest("John", "john@test.com", 1L);
        MemberResponse response = new MemberResponse(1L, "John", "john@test.com", LocalDate.now(), MemberStatus.ACTIVE, "Plan", "Gym");

        when(memberService.registerMember(any(MemberRegistrationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John"));
    }
}
