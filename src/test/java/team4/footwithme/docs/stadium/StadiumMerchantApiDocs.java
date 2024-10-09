package team4.footwithme.docs.stadium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.security.WithMockPrincipalDetail;
import team4.footwithme.stadium.api.StadiumMerchantApi;
import team4.footwithme.stadium.api.request.StadiumRegisterRequest;
import team4.footwithme.stadium.api.request.StadiumUpdateRequest;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;

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

public class StadiumMerchantApiDocs extends RestDocsSupport {

    private final StadiumService stadiumService = mock(StadiumService.class);

    @Override
    protected Object initController() {return new StadiumMerchantApi(stadiumService);}

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("풋살장 등록 API")
    void registerStadium() throws Exception {
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

        given(stadiumService.registerStadium(any(StadiumRegisterServiceRequest.class), any())).willReturn(response);

        mockMvc.perform(post("/api/v1/merchant/stadium/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("stadium-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("description").type(JsonFieldType.STRING).optional().description("풋살장 설명"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).optional().description("회원 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("풋살장 설명"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("경도")
                        )
                ));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("풋살장 수정 API")
    void updateStadium() throws Exception {
        Long stadiumId = 1L;
        StadiumUpdateRequest request = new StadiumUpdateRequest(
                "Updated Stadium",
                "456 Updated Address",
                "010-9876-5432",
                "Updated Description",
                35.1796,
                129.0756
        );
        StadiumDetailResponse response = new StadiumDetailResponse(
                stadiumId,
                null,
                "Updated Stadium",
                "456 Updated Address",
                "010-9876-5432",
                "Updated Description",
                35.1796,
                129.0756
        );

        given(stadiumService.updateStadium(any(), any(), eq(stadiumId))).willReturn(response);

        mockMvc.perform(put("/api/v1/merchant/stadium/{stadiumId}", stadiumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("stadium-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stadiumId").description("수정할 풋살장 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("description").type(JsonFieldType.STRING).optional().description("풋살장 설명"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).optional().description("회원 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("풋살장 설명"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("경도")
                        )
                ));
    }

    @Test
    @WithMockPrincipalDetail(email = "merchant@example.com", role = team4.footwithme.member.domain.MemberRole.MERCHANT)
    @DisplayName("풋살장 삭제 API")
    void deleteStadium() throws Exception {
        Long stadiumId = 1L;

        mockMvc.perform(delete("/api/v1/merchant/stadium/{stadiumId}", stadiumId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stadium-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stadiumId").description("삭제할 풋살장 ID")
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