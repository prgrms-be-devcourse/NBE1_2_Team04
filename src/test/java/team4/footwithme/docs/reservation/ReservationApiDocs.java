package team4.footwithme.docs.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.resevation.api.ReservationApi;
import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.resevation.service.ReservationService;
import team4.footwithme.resevation.service.response.ParticipantInfoResponse;
import team4.footwithme.resevation.service.response.ReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.ReservationInfoResponse;
import team4.footwithme.resevation.service.response.ReservationsResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationApiDocs extends RestDocsSupport {

    private final ReservationService reservationService = mock(ReservationService.class);

    @Override
    protected Object initController() {
        return new ReservationApi(reservationService);
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("GET /api/v1/reservation/ready - 준비 중인 예약 목록을 가져온다")
    void getReadyReservations() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reservationId"));
        Slice<ReservationsResponse> reservations = new SliceImpl<>(Collections.singletonList(
            new ReservationsResponse(1L, 1L, 1L, 1L, LocalDateTime.parse("2024-10-01T10:00"), team4.footwithme.resevation.domain.ParticipantGender.MALE)
        ), pageable, false);

        given(reservationService.findReadyReservations(any(), any())).willReturn(reservations);

        mockMvc.perform(get("/api/v1/reservation/ready")
                .param("reservationId", "1")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].reservationId").value(1L))
            .andDo(document("reservation-get-ready",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("reservationId").description("예약 ID"),
                    parameterWithName("page").optional().description("페이지 번호 (기본값은 0)")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data.content[].reservationId").type(JsonFieldType.NUMBER).description("예약 ID"),
                    fieldWithPath("data.content[].courtId").type(JsonFieldType.NUMBER).description("코트 ID"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                    fieldWithPath("data.content[].teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                    fieldWithPath("data.content[].matchDate").type(JsonFieldType.STRING).description("매치 날짜"),
                    fieldWithPath("data.content[].gender").type(JsonFieldType.STRING).description("참여 성별"),
                    fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
                    fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                    fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                    fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                    fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                    fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                    fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                    fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징되지 않음 여부"),
                    fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
                    fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                    fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                    fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                    fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
                    fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비어있는지 여부")
                )
            ));
    }

    @DisplayName("팀 일정 전체조회 API")
    @Test
    void getTeamReservationInfo() throws Exception {
        Long teamId = 1L;

        List<ReservationInfoResponse> response = List.of(
            new ReservationInfoResponse(
                "구장1",
                LocalDateTime.of(2024, 11, 12, 15, 0, 0),
                ReservationStatus.RECRUITING
            ),
            new ReservationInfoResponse(
                "구장2",
                LocalDateTime.of(2024, 11, 20, 13, 0, 0),
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

        ReservationInfoDetailsResponse response = new ReservationInfoDetailsResponse(
            "구장 1",
            "상대 팀",
            LocalDateTime.of(2024, 11, 12, 15, 0, 0),
            List.of(
                new ParticipantInfoResponse("팀원1", ParticipantRole.MEMBER),
                new ParticipantInfoResponse("팀원2", ParticipantRole.MEMBER)
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

    @DisplayName("예약 취소하는 API")
    @Test
    void deleteReservation() throws Exception {
        //given
        Long reservationId = 1L;

        //when
        given(reservationService.deleteReservation(eq(reservationId), any()))
            .willReturn(reservationId);

        //then
        mockMvc.perform(delete("/api/v1/reservation/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN"))
            .andExpect(status().isOk())
            .andDo(document("reservation-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("reservationId").description("예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("삭제된 예약 ID")
                )
            ));
    }

}