package team4.footwithme.stadium.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.security.WithMockPrincipalDetail;
import team4.footwithme.stadium.api.request.CourtDeleteRequest;
import team4.footwithme.stadium.api.request.CourtRegisterRequest;
import team4.footwithme.stadium.api.request.CourtUpdateRequest;
import team4.footwithme.stadium.service.response.CourtDetailResponse;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CourtMerchantApiTest extends ApiTestSupport {

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("구장 등록 시 Created 상태를 반환해야 한다")
    void registerCourt_shouldReturnCreatedStatus() throws Exception {
        CourtRegisterRequest request = new CourtRegisterRequest(
            1L,
            "Test Court",
            "Test Description",
            new BigDecimal("5000.00")
        );
        CourtDetailResponse response = new CourtDetailResponse(
            1L,
            1L,
            "Test Court",
            "Test Description",
            new BigDecimal("5000.00")
        );

        Mockito.when(courtService.registerCourt(any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/merchant/court/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("구장 수정 시 OK 상태를 반환해야 한다")
    void updateCourt_shouldReturnOkStatus() throws Exception {
        CourtUpdateRequest request = new CourtUpdateRequest(
            1L,
            "Updated Court",
            "Updated Description",
            new BigDecimal("6000.00")
        );
        CourtDetailResponse response = new CourtDetailResponse(
            1L,
            1L,
            "Updated Court",
            "Updated Description",
            new BigDecimal("6000.00")
        );

        Mockito.when(courtService.updateCourt(any(), any(), eq(1L))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/merchant/court/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("구장 삭제 시 OK 상태를 반환해야 한다")
    void deleteCourt_shouldReturnOkStatus() throws Exception {
        CourtDeleteRequest request = new CourtDeleteRequest(
            1L
        );

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/merchant/court/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());

        Mockito.verify(courtService).deleteCourt(any(), any(), eq(1L));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("유효성 검사 실패 시 Bad Request 상태를 반환해야 한다")
    void registerCourt_shouldReturnBadRequestStatus_whenValidationFails() throws Exception {
        CourtRegisterRequest request = new CourtRegisterRequest(
            null,  // stadiumId is null
            "",  // name is blank
            "Test Description",
            new BigDecimal("-1000.00")  // price_per_hour is negative
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/merchant/court/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").exists());
    }
}