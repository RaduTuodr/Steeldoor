package org.example.steeldoor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.steeldoor.config.JwtUtils;
import org.example.steeldoor.dto.CreateInterviewRoundDTO;
import org.example.steeldoor.model.InterviewRound;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.enums.RoundType;
import org.example.steeldoor.service.InterviewRoundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = InterviewRoundController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class InterviewRoundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtils jwtUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private InterviewRoundService interviewRoundService;

    @Test
    void getInterviewRound_returnsDto() throws Exception {
        Submission submission = Submission.builder().id(2).build();
        InterviewRound round = InterviewRound.builder()
                .id(1)
                .submission(submission)
                .type(RoundType.OA)
                .title("T")
                .description("D")
                .difficulty(3)
                .durationMinutes(45)
                .orderIndex(1)
                .build();

        when(interviewRoundService.getInterviewRound(1)).thenReturn(round);

        mockMvc.perform(get("/api/round/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.submissionId").value(2))
                .andExpect(jsonPath("$.roundType").value(RoundType.OA.getDbValue()))
                .andExpect(jsonPath("$.title").value("T"));
    }

    @Test
    void createInterviewRound_success_returnsCreated() throws Exception {
        CreateInterviewRoundDTO dto = CreateInterviewRoundDTO.builder()
                .submissionId(2)
                .roundType(RoundType.OA.getDbValue())
                .title("T")
                .description("D")
                .difficulty(2)
                .durationMinutes(30)
                .orderIndex(1)
                .build();

        Submission submission = Submission.builder().id(2).build();
        InterviewRound result = InterviewRound.builder()
                .id(99)
                .submission(submission)
                .type(RoundType.OA)
                .title("T")
                .orderIndex(1)
                .build();

        when(interviewRoundService.createInterviewRound(any(CreateInterviewRoundDTO.class))).thenReturn(result);

        mockMvc.perform(post("/api/round")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/round/99"))
                .andExpect(jsonPath("$.id").value(99));
    }

    @Test
    void createInterviewRound_validationFailure_returnsBadRequest() throws Exception {
        String invalidJson = "{\"roundType\":\"OA\", \"title\":\"T\"}";

        mockMvc.perform(post("/api/round")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllInterviewRoundsBySubmissionId_returnsList() throws Exception {
        Submission submission = Submission.builder().id(2).build();
        InterviewRound round = InterviewRound.builder().id(3).submission(submission).type(RoundType.OA).orderIndex(1).build();

        when(interviewRoundService.getInterviewRoundsBySubmissionId(2)).thenReturn(List.of(round));

        mockMvc.perform(get("/api/round").param("submissionId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].submissionId").value(2));
    }

    @Test
    void orderInterviewRound_callsService() throws Exception {
        doReturn(null).when(interviewRoundService).updateOrderIndex(5, 2);

        mockMvc.perform(post("/api/round/5/order/2"))
                .andExpect(status().isOk());

        verify(interviewRoundService).updateOrderIndex(5, 2);
    }
}

