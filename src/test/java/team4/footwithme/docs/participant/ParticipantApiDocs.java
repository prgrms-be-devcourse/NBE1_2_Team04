package team4.footwithme.docs.participant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.resevation.api.ParticipantApi;
import team4.footwithme.resevation.api.request.ParticipantUpdateRequest;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.service.ParticipantService;
import team4.footwithme.resevation.service.request.ParticipantUpdateServiceRequest;
import team4.footwithme.resevation.service.response.ParticipantMemberInfo;
import team4.footwithme.resevation.service.response.ParticipantResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParticipantApiDocs extends RestDocsSupport {
    private final ParticipantService participantService = mock(ParticipantService.class);

    @Override
    protected Object initController() {
        return new ParticipantApi(participantService);
    }

    //매칭 예약 용병 인원을 추가할 수 있다.
    @DisplayName("매칭 예약 용병 인원을 추가할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void applyMercenary() throws Exception {
        Long mercenaryId = 1L;

        ParticipantResponse response = new ParticipantResponse(
            1L,
            1L,
            ParticipantRole.PENDING,
            new ParticipantMemberInfo(
                1L,
                "a@a.com",
                "이름1",
                MemberRole.USER
            )
        );

        given(participantService.createMercenaryParticipant(any(Long.class), any()))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/participant/mercenary/{mercenaryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("participant-create-mercenary",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("mercenaryId").description("참가할 용병 게시판 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.participantId").type(JsonFieldType.NUMBER)
                        .description("예약 참여 인원 ID"),
                    fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER)
                        .description("예약 ID"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING)
                        .description("예약 참여 인원 역할"),
                    fieldWithPath("data.memberInfo").type(JsonFieldType.OBJECT)
                        .description("멤버 데이터"),
                    fieldWithPath("data.memberInfo.memberId").type(JsonFieldType.NUMBER)
                        .description("멤버 ID"),
                    fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("data.memberInfo.name").type(JsonFieldType.STRING)
                        .description("멤버 이름"),
                    fieldWithPath("data.memberInfo.memberRole").type(JsonFieldType.STRING)
                        .description("멤버 역할"))));
    }

    //팀원은 매칭 예약 인원으로 참가할 수 있다.
    @DisplayName("팀원은 매칭 예약 인원으로 참가할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void joinReservation() throws Exception {
        Long reservationId = 1L;

        ParticipantResponse response = new ParticipantResponse(
            1L,
            1L,
            ParticipantRole.PENDING,
            new ParticipantMemberInfo(
                1L,
                "a@a.com",
                "이름1",
                MemberRole.USER
            )
        );

        given(participantService.createParticipant(any(Long.class), any()))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/participant/reservation/join/{reservationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("participant-create-reservation",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("reservationId").description("참가할 예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.participantId").type(JsonFieldType.NUMBER)
                        .description("예약 참여 인원 ID"),
                    fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER)
                        .description("예약 ID"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING)
                        .description("예약 참여 인원 역할"),
                    fieldWithPath("data.memberInfo").type(JsonFieldType.OBJECT)
                        .description("멤버 데이터"),
                    fieldWithPath("data.memberInfo.memberId").type(JsonFieldType.NUMBER)
                        .description("멤버 ID"),
                    fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("data.memberInfo.name").type(JsonFieldType.STRING)
                        .description("멤버 이름"),
                    fieldWithPath("data.memberInfo.memberRole").type(JsonFieldType.STRING)
                        .description("멤버 역할"))));
    }

    //매칭 예약 인원은 매칭 예약 탈퇴를 할 수 있다.
    @DisplayName("매칭 예약 인원은 매칭 예약 탈퇴를 할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void leaveReservation() throws Exception {
        Long reservationId = 1L;

        given(participantService.deleteParticipant(any(Long.class), any()))
            .willReturn(reservationId);

        mockMvc.perform(delete("/api/v1/participant/reservation/leave/{reservationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("participant-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("reservationId").description("탈퇴할 예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("탈퇴한 회원 ID"))));
    }

    //예약장은 매칭 예약 인원 역할을 수정할 수 있다.
    @DisplayName("예약장은 매칭 예약 인원 역할을 수정할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void updateParticipant() throws Exception {
        Long participantId = 1L;
        ParticipantUpdateRequest request = new ParticipantUpdateRequest(
            participantId,
            ParticipantRole.ACCEPT
        );

        ParticipantResponse response = new ParticipantResponse(
            1L,
            1L,
            ParticipantRole.ACCEPT,
            new ParticipantMemberInfo(
                1L,
                "a@a.com",
                "이름1",
                MemberRole.USER
            )
        );

        given(participantService.updateMercenaryParticipant(any(ParticipantUpdateServiceRequest.class), any()))
            .willReturn(response);

        mockMvc.perform(put("/api/v1/participant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("participant-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("participantId").description("수정 할 예약 참여 인원 ID"),
                    fieldWithPath("role").description("수정 할 권한")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.participantId").type(JsonFieldType.NUMBER)
                        .description("예약 참여 인원 ID"),
                    fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER)
                        .description("예약 ID"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING)
                        .description("예약 참여 인원 역할"),
                    fieldWithPath("data.memberInfo").type(JsonFieldType.OBJECT)
                        .description("멤버 데이터"),
                    fieldWithPath("data.memberInfo.memberId").type(JsonFieldType.NUMBER)
                        .description("멤버 ID"),
                    fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("data.memberInfo.name").type(JsonFieldType.STRING)
                        .description("멤버 이름"),
                    fieldWithPath("data.memberInfo.memberRole").type(JsonFieldType.STRING)
                        .description("멤버 역할"))));


    }

    //매칭 예약 확정 참가자를 조회할 수 있다.
    @DisplayName("매칭 예약 확정 참가자를 조회할 수 있다.")
    @Test
    void getAcceptParticipant() throws Exception {
        Long reservationId = 1L;

        ParticipantResponse response = new ParticipantResponse(
            1L,
            1L,
            ParticipantRole.ACCEPT,
            new ParticipantMemberInfo(
                1L,
                "a@a.com",
                "이름1",
                MemberRole.USER
            )
        );

        List<ParticipantResponse> participantResponses = new ArrayList<>();

        participantResponses.add(response);

        given(participantService.getAcceptParticipants(any(Long.class)))
            .willReturn(participantResponses);

        mockMvc.perform(get("/api/v1/participant/accept/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("participant-get-accept",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("reservationId").description("조회할 예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY)
                        .description("응답 데이터"),
                    fieldWithPath("data[].participantId").type(JsonFieldType.NUMBER)
                        .description("예약 참여 인원 ID"),
                    fieldWithPath("data[].reservationId").type(JsonFieldType.NUMBER)
                        .description("예약 ID"),
                    fieldWithPath("data[].role").type(JsonFieldType.STRING)
                        .description("예약 참여 인원 역할"),
                    fieldWithPath("data[].memberInfo").type(JsonFieldType.OBJECT)
                        .description("멤버 데이터"),
                    fieldWithPath("data[].memberInfo.memberId").type(JsonFieldType.NUMBER)
                        .description("멤버 ID"),
                    fieldWithPath("data[].memberInfo.email").type(JsonFieldType.STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("data[].memberInfo.name").type(JsonFieldType.STRING)
                        .description("멤버 이름"),
                    fieldWithPath("data[].memberInfo.memberRole").type(JsonFieldType.STRING)
                        .description("멤버 역할"))));
    }

    //매칭 예약 용병 참가자를 조회할 수 있다.
    @DisplayName("매칭 예약 용병 참가자를 조회할 수 있다.")
    @Test
    void getPendingParticipant() throws Exception {
        Long reservationId = 1L;

        ParticipantResponse response = new ParticipantResponse(
            1L,
            1L,
            ParticipantRole.PENDING,
            new ParticipantMemberInfo(
                1L,
                "a@a.com",
                "이름1",
                MemberRole.USER
            )
        );

        List<ParticipantResponse> participantResponses = new ArrayList<>();

        participantResponses.add(response);

        given(participantService.getParticipantsMercenary(any(Long.class)))
            .willReturn(participantResponses);

        mockMvc.perform(get("/api/v1/participant/pending/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("participant-get-pending",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("reservationId").description("조회할 예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY)
                        .description("응답 데이터"),
                    fieldWithPath("data[].participantId").type(JsonFieldType.NUMBER)
                        .description("예약 참여 인원 ID"),
                    fieldWithPath("data[].reservationId").type(JsonFieldType.NUMBER)
                        .description("예약 ID"),
                    fieldWithPath("data[].role").type(JsonFieldType.STRING)
                        .description("예약 참여 인원 역할"),
                    fieldWithPath("data[].memberInfo").type(JsonFieldType.OBJECT)
                        .description("멤버 데이터"),
                    fieldWithPath("data[].memberInfo.memberId").type(JsonFieldType.NUMBER)
                        .description("멤버 ID"),
                    fieldWithPath("data[].memberInfo.email").type(JsonFieldType.STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("data[].memberInfo.name").type(JsonFieldType.STRING)
                        .description("멤버 이름"),
                    fieldWithPath("data[].memberInfo.memberRole").type(JsonFieldType.STRING)
                        .description("멤버 역할"))));
    }

    //매칭 예약 전체 참가자를 조회할 수 있다.
    @DisplayName("매칭 예약 전체 참가자를 조회할 수 있다.")
    @Test
    void getAllParticipant() throws Exception {
        Long reservationId = 1L;

        ParticipantResponse response = new ParticipantResponse(
            1L,
            1L,
            ParticipantRole.IGNORE,
            new ParticipantMemberInfo(
                1L,
                "a@a.com",
                "이름1",
                MemberRole.USER
            )
        );

        List<ParticipantResponse> participantResponses = new ArrayList<>();

        participantResponses.add(response);

        given(participantService.getParticipants(any(Long.class)))
            .willReturn(participantResponses);

        mockMvc.perform(get("/api/v1/participant/all/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("participant-get-all",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("reservationId").description("조회할 예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY)
                        .description("응답 데이터"),
                    fieldWithPath("data[].participantId").type(JsonFieldType.NUMBER)
                        .description("예약 참여 인원 ID"),
                    fieldWithPath("data[].reservationId").type(JsonFieldType.NUMBER)
                        .description("예약 ID"),
                    fieldWithPath("data[].role").type(JsonFieldType.STRING)
                        .description("예약 참여 인원 역할"),
                    fieldWithPath("data[].memberInfo").type(JsonFieldType.OBJECT)
                        .description("멤버 데이터"),
                    fieldWithPath("data[].memberInfo.memberId").type(JsonFieldType.NUMBER)
                        .description("멤버 ID"),
                    fieldWithPath("data[].memberInfo.email").type(JsonFieldType.STRING)
                        .description("멤버 이메일"),
                    fieldWithPath("data[].memberInfo.name").type(JsonFieldType.STRING)
                        .description("멤버 이름"),
                    fieldWithPath("data[].memberInfo.memberRole").type(JsonFieldType.STRING)
                        .description("멤버 역할"))));
    }
}
