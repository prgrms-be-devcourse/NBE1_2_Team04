package team4.footwithme.resevation.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.resevation.api.request.GameRegisterRequest;
import team4.footwithme.resevation.api.request.GameStatusUpdateRequest;
import team4.footwithme.resevation.service.response.GameDetailResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameApiTest extends ApiTestSupport {

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 등록 시 Created 상태를 반환해야 한다")
    void registerGame_shouldReturnCreatedStatus() throws Exception {
        GameRegisterRequest request = new GameRegisterRequest(1L, 2L);
        GameDetailResponse response = new GameDetailResponse(1L, 1L, 2L, team4.footwithme.resevation.domain.GameStatus.READY);

        Mockito.when(gameService.registerGame(any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/game/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 상태 수정 시 OK 상태를 반환해야 한다")
    void updateGameStatus_shouldReturnOkStatus() throws Exception {
        GameStatusUpdateRequest request = new GameStatusUpdateRequest(1L, team4.footwithme.resevation.domain.GameStatus.READY);

        Mockito.when(gameService.updateGameStatus(any(), any())).thenReturn("게임 상태가 업데이트되었습니다.");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/game/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 등록 시 유효성 검사 실패 시 Bad Request 상태를 반환해야 한다")
    void registerGame_shouldReturnBadRequestStatus_whenValidationFails() throws Exception {
        GameRegisterRequest request = new GameRegisterRequest(null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/game/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 상태 수정 시 유효성 검사 실패 시 Bad Request 상태를 반환해야 한다")
    void updateGameStatus_shouldReturnBadRequestStatus_whenValidationFails() throws Exception {
        GameStatusUpdateRequest request = new GameStatusUpdateRequest(1L, null); // 상태 필드가 누락됨

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/game/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").exists());
    }
}