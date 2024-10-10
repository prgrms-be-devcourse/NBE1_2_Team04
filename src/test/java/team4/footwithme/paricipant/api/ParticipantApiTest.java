package team4.footwithme.paricipant.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.resevation.api.request.ParticipantUpdateRequest;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.service.request.ParticipantUpdateServiceRequest;
import team4.footwithme.resevation.service.response.ParticipantMemberInfo;
import team4.footwithme.resevation.service.response.ParticipantResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParticipantApiTest extends ApiTestSupport {

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

        given(participantService.createMercenaryParticipant(any(Long.class), any(Member.class)))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/participant/mercenary/{mercenaryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.participantId").value(1L))
            .andExpect(jsonPath("$.data.reservationId").value(1L))
            .andExpect(jsonPath("$.data.role").value("PENDING"))
            .andExpect(jsonPath("$.data.memberInfo").isMap())
            .andExpect(jsonPath("$.data.memberInfo.memberId").value(1L))
            .andExpect(jsonPath("$.data.memberInfo.email").value("a@a.com"))
            .andExpect(jsonPath("$.data.memberInfo.name").value("이름1"))
            .andExpect(jsonPath("$.data.memberInfo.memberRole").value("USER"));
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

        given(participantService.createParticipant(any(Long.class), any(Member.class)))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/participant/reservation/join/{reservationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.participantId").value(1L))
            .andExpect(jsonPath("$.data.reservationId").value(1L))
            .andExpect(jsonPath("$.data.role").value("PENDING"))
            .andExpect(jsonPath("$.data.memberInfo").isMap())
            .andExpect(jsonPath("$.data.memberInfo.memberId").value(1L))
            .andExpect(jsonPath("$.data.memberInfo.email").value("a@a.com"))
            .andExpect(jsonPath("$.data.memberInfo.name").value("이름1"))
            .andExpect(jsonPath("$.data.memberInfo.memberRole").value("USER"));
    }

    //매칭 예약 인원은 매칭 예약 탈퇴를 할 수 있다.
    @DisplayName("매칭 예약 인원은 매칭 예약 탈퇴를 할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void leaveReservation() throws Exception {
        Long reservationId = 1L;

        given(participantService.deleteParticipant(any(Long.class), any(Member.class)))
            .willReturn(reservationId);

        mockMvc.perform(delete("/api/v1/participant/reservation/leave/{reservationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value(1L));
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

        given(participantService.updateMercenaryParticipant(any(ParticipantUpdateServiceRequest.class), any(Member.class)))
            .willReturn(response);

        mockMvc.perform(put("/api/v1/participant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.participantId").value(1L))
            .andExpect(jsonPath("$.data.reservationId").value(1L))
            .andExpect(jsonPath("$.data.role").value("ACCEPT"))
            .andExpect(jsonPath("$.data.memberInfo").isMap())
            .andExpect(jsonPath("$.data.memberInfo.memberId").value(1L))
            .andExpect(jsonPath("$.data.memberInfo.email").value("a@a.com"))
            .andExpect(jsonPath("$.data.memberInfo.name").value("이름1"))
            .andExpect(jsonPath("$.data.memberInfo.memberRole").value("USER"));


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
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].participantId").value(1L))
            .andExpect(jsonPath("$.data[0].reservationId").value(1L))
            .andExpect(jsonPath("$.data[0].role").value("ACCEPT"))
            .andExpect(jsonPath("$.data[0].memberInfo").isMap())
            .andExpect(jsonPath("$.data[0].memberInfo.memberId").value(1L))
            .andExpect(jsonPath("$.data[0].memberInfo.email").value("a@a.com"))
            .andExpect(jsonPath("$.data[0].memberInfo.name").value("이름1"))
            .andExpect(jsonPath("$.data[0].memberInfo.memberRole").value("USER"));
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
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].participantId").value(1L))
            .andExpect(jsonPath("$.data[0].reservationId").value(1L))
            .andExpect(jsonPath("$.data[0].role").value("PENDING"))
            .andExpect(jsonPath("$.data[0].memberInfo").isMap())
            .andExpect(jsonPath("$.data[0].memberInfo.memberId").value(1L))
            .andExpect(jsonPath("$.data[0].memberInfo.email").value("a@a.com"))
            .andExpect(jsonPath("$.data[0].memberInfo.name").value("이름1"))
            .andExpect(jsonPath("$.data[0].memberInfo.memberRole").value("USER"));
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
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].participantId").value(1L))
            .andExpect(jsonPath("$.data[0].reservationId").value(1L))
            .andExpect(jsonPath("$.data[0].role").value("IGNORE"))
            .andExpect(jsonPath("$.data[0].memberInfo").isMap())
            .andExpect(jsonPath("$.data[0].memberInfo.memberId").value(1L))
            .andExpect(jsonPath("$.data[0].memberInfo.email").value("a@a.com"))
            .andExpect(jsonPath("$.data[0].memberInfo.name").value("이름1"))
            .andExpect(jsonPath("$.data[0].memberInfo.memberRole").value("USER"));
    }
}
