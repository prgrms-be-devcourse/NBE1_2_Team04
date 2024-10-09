package team4.footwithme.docs.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.resevation.api.YYReservationApi;
import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.resevation.service.YYReservationService;
import team4.footwithme.resevation.service.response.YYParticipantResponse;
import team4.footwithme.resevation.service.response.YYReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.YYReservationInfoResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

public class YYReservationApiDocs extends RestDocsSupport {

    private final YYReservationService reservationService = mock(YYReservationService.class);

    @Override
    protected Object initController() {
        return new YYReservationApi(reservationService);
    }

    @DisplayName("팀 일정 전체조회 API")
    @Test
    void getTeamReservationInfo() throws Exception {
        Long teamId = 1L;

        List<YYReservationInfoResponse> response = List.of(
            new YYReservationInfoResponse(
                    "구장1",
                    LocalDateTime.of(2024, 11,12,15,0,0),
                    ReservationStatus.RECRUITING
            ),
            new YYReservationInfoResponse(
                    "구장2",
                    LocalDateTime.of(2024, 11,20,13,0,0),
                    ReservationStatus.RECRUITING
            )
        );

        given(reservationService.getTeamReservationInfo(teamId))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/reservation/{teamId}", teamId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reservation-info-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("팀 일정 리스트"),
                                fieldWithPath("data[].courtName").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data[].matchDate").type(JsonFieldType.ARRAY).description("예약 날짜"),
                                fieldWithPath("data[].status").type(JsonFieldType.STRING).description("진행 상태")
                        )
                ));
    }

    @DisplayName("팀 일정 상세 조회 API")
    @Test
    void getTeamReservationInfoDetails() throws Exception {
        long reservationId = 1L;

        YYReservationInfoDetailsResponse response = new YYReservationInfoDetailsResponse(
                "구장 1",
                "상대 팀",
                LocalDateTime.of(2024, 11,12,15,0,0),
                List.of(
                        new YYParticipantResponse("팀원1", ParticipantRole.MEMBER),
                        new YYParticipantResponse("팀원2", ParticipantRole.MEMBER)
                ),
                ParticipantGender.MALE,
                ReservationStatus.CONFIRMED
        );

        given(reservationService.getTeamReservationInfoDetails(reservationId))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/reservation/details/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reservation-detailInfo-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reservationId").description("예약 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.courtName").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data.matchTeamName").type(JsonFieldType.STRING).description("상대팀 이름"),
                                fieldWithPath("data.matchDate").type(JsonFieldType.ARRAY).description("예약 날짜"),
                                fieldWithPath("data.participants").type(JsonFieldType.ARRAY).description("참여자 리스트"),
                                fieldWithPath("data.participants[].memberName").type(JsonFieldType.STRING).description("참여자 이름"),
                                fieldWithPath("data.participants[].role").type(JsonFieldType.STRING).description("참여자 상태"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING).description("참여인원 성비"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("진행 상태")
                        )
                ));
    }
}
