package team4.footwithme.stadium.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.security.WithMockPrincipalDetail;
import team4.footwithme.stadium.api.request.StadiumRegisterRequest;
import team4.footwithme.stadium.api.request.StadiumUpdateRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StadiumMerchantApiTest extends ApiTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("풋살장 등록 시 Created 상태를 반환해야 한다")
    void registerStadium_shouldReturnCreatedStatus() throws Exception {
        StadiumRegisterRequest request = new StadiumRegisterRequest(
            "Test Stadium",
            "123 Test Address",
            "010-1234-5678",
            "Test Description",
            37.5665,
            126.9780
        );
        StadiumDetailResponse response = new StadiumDetailResponse(
            1L,
            1L,
            "Test Stadium",
            "123 Test Address",
            "010-1234-5678",
            "Test Description",
            37.5665,
            126.9780
        );

        Mockito.when(stadiumService.registerStadium(any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/merchant/stadium/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("풋살장 수정 시 OK 상태를 반환해야 한다")
    void updateStadium_shouldReturnOkStatus() throws Exception {
        StadiumUpdateRequest request = new StadiumUpdateRequest(
            "Updated Stadium",
            "456 Updated Address",
            "010-9876-5432",
            "Updated Description",
            35.1796,
            129.0756
        );
        StadiumDetailResponse response = new StadiumDetailResponse(
            1L,
            1L,
            "Updated Stadium",
            "456 Updated Address",
            "010-9876-5432",
            "Updated Description",
            35.1796,
            129.0756
        );

        Mockito.when(stadiumService.updateStadium(any(), any(), eq(1L))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/merchant/stadium/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("풋살장 삭제 시 OK 상태를 반환해야 한다")
    void deleteStadium_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/merchant/stadium/1"))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(stadiumService).deleteStadium(any(), eq(1L));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("유효성 검사 실패 시 Bad Request 상태를 반환해야 한다")
    void registerStadium_shouldReturnBadRequestStatus_whenValidationFails() throws Exception {
        StadiumRegisterRequest request = new StadiumRegisterRequest(
            "",
            "",
            "invalid-phone",
            "Test Description",
            null,
            200.0
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/merchant/stadium/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").exists());
    }
}