package team4.footwithme.docs.stadium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.security.WithMockPrincipalDetail;
import team4.footwithme.stadium.api.CourtMerchantApi;
import team4.footwithme.stadium.api.request.CourtDeleteRequest;
import team4.footwithme.stadium.api.request.CourtRegisterRequest;
import team4.footwithme.stadium.api.request.CourtUpdateRequest;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest;
import team4.footwithme.stadium.service.response.CourtDetailResponse;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourtMerchantApiDocs extends RestDocsSupport {

    private final CourtService courtService = mock(CourtService.class);

    @Override
    protected Object initController() {
        return new CourtMerchantApi(courtService);
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("구장 등록 API")
    void registerCourt() throws Exception {
        CourtRegisterRequest request = new CourtRegisterRequest(
                1L,
                "Test Court",
                "Test Description",
                new BigDecimal(50000)
        );
        CourtDetailResponse response = new CourtDetailResponse(
                1L,
                1L,
                "Test Court",
                "Test Description",
                new BigDecimal(50000)
        );

        given(courtService.registerCourt(any(CourtRegisterServiceRequest.class), any())).willReturn(response);

        mockMvc.perform(post("/api/v1/merchant/court/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("court-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).optional().description("구장 설명"),
                                fieldWithPath("price_per_hour").type(JsonFieldType.NUMBER).description("시간당 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.courtId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("구장 설명"),
                                fieldWithPath("data.price_per_hour").type(JsonFieldType.NUMBER).description("시간당 가격")
                        )
                ));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("구장 수정 API")
    void updateCourt() throws Exception {
        Long courtId = 1L;
        CourtUpdateRequest request = new CourtUpdateRequest(
                1L,
                "Updated Court",
                "Updated Description",
                new BigDecimal(60000)
        );
        CourtDetailResponse response = new CourtDetailResponse(
                courtId,
                1L,
                "Updated Court",
                "Updated Description",
                new BigDecimal(60000)
        );

        given(courtService.updateCourt(any(), any(), eq(courtId))).willReturn(response);

        mockMvc.perform(put("/api/v1/merchant/court/{courtId}", courtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("court-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("courtId").description("수정할 구장 ID")
                        ),
                        requestFields(
                                fieldWithPath("stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).optional().description("구장 설명"),
                                fieldWithPath("price_per_hour").type(JsonFieldType.NUMBER).description("시간당 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.courtId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("구장 설명"),
                                fieldWithPath("data.price_per_hour").type(JsonFieldType.NUMBER).description("시간당 가격")
                        )
                ));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("구장 삭제 API")
    void deleteCourt() throws Exception {
        Long courtId = 1L;
        CourtDeleteRequest request = new CourtDeleteRequest(1L);

        mockMvc.perform(delete("/api/v1/merchant/court/{courtId}", courtId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("court-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("courtId").description("삭제할 구장 ID")
                        ),
                        requestFields(
                                fieldWithPath("stadiumId").type(JsonFieldType.NUMBER).description("구장의 풋살장 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("삭제된 데이터 (항상 null)")
                        )
                ));
    }
}