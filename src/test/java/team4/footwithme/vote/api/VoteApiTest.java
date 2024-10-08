package team4.footwithme.vote.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.config.SecurityConfig;
import team4.footwithme.member.domain.Member;
import team4.footwithme.security.WithMockPrincipalDetail;
import team4.footwithme.vote.api.request.CourtChoices;
import team4.footwithme.vote.api.request.VoteCourtCreateRequest;
import team4.footwithme.vote.service.request.VoteCourtCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteApiTest extends ApiTestSupport {

    @DisplayName("새로운 구장 투표를 생성한다.")
    @WithMockPrincipalDetail(email = "test@gmail.com")
    @Test
    void createLocateVote() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        CourtChoices courtChoices1 = new CourtChoices(1L);
        CourtChoices courtChoices2 = new CourtChoices(2L);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest("연말 행사 투표", endAt, List.of(courtChoices1, courtChoices2));


        VoteResponse response = new VoteResponse(
            1L,
            "연말 행사 투표",
            endAt,
            "진행 중",
            List.of(new VoteItemResponse(1L, "최강 풋살장", List.of(1L)),
                new VoteItemResponse(2L, "열정 풋살장", List.of(1L))
            )
        );

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any()))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.voteId").value(1L))
            .andExpect(jsonPath("$.data.title").value("연말 행사 투표"))
            .andExpect(jsonPath("$.data.endAt").value(endAt.toString()))
            .andExpect(jsonPath("$.data.voteStatus").value("진행 중"))
            .andExpect(jsonPath("$.data.choices").isArray())
            .andExpect(jsonPath("$.data.choices[0].voteItemId").value(1L))
            .andExpect(jsonPath("$.data.choices[0].content").value("최강 풋살장"))
            .andExpect(jsonPath("$.data.choices[0].memberIds[0]").value(1L))
            .andExpect(jsonPath("$.data.choices[1].voteItemId").value(2L))
            .andExpect(jsonPath("$.data.choices[1].content").value("열정 풋살장"))
            .andExpect(jsonPath("$.data.choices[1].memberIds[0]").value(1L));

    }

    @DisplayName("새로운 구장 투표를 생성 할 때 제목은 필수이다.")
    @Test
    void createLocateVoteWhenTitleIsNotExistThenThrowException() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        CourtChoices courtChoices1 = new CourtChoices(1L);
        CourtChoices courtChoices2 = new CourtChoices(2L);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest(null, endAt, List.of(courtChoices1, courtChoices2));

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any()))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of()),
                    new VoteItemResponse(2L, "열정 풋살장", List.of())
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L).with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("제목은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("새로운 구장 투표를 생성 할 때 제목은 50자 이하이다.")
    @WithMockUser
    @Test
    void createLocateVoteWhenTitleIsOverLengthThenThrowException() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        CourtChoices courtChoices1 = new CourtChoices(1L);
        CourtChoices courtChoices2 = new CourtChoices(2L);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest("a".repeat(51), endAt, List.of(courtChoices1, courtChoices2));

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any()))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of()),
                    new VoteItemResponse(2L, "열정 풋살장", List.of())
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L).with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("제목은 50자 이하여야 합니다."))
            .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("새로운 구장 투표를 생성 할 때 시간은 현재 시간보다 미래의 시간으로 지정해야한다.")
    @WithMockPrincipalDetail(email = "test@gmail.com")
    @Test
    void createLocateVoteWhenEndAtIsBeforeNowThenThrowException() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().minusDays(1);
        CourtChoices courtChoices1 = new CourtChoices(1L);
        CourtChoices courtChoices2 = new CourtChoices(2L);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest("연말 행사 투표", endAt, List.of(courtChoices1, courtChoices2));

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any()))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of()),
                    new VoteItemResponse(2L, "열정 풋살장", List.of())
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L).with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("투표 종료 시간은 현재 시간보다 미래의 시간으로 지정해야합니다."))
            .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("새로운 구장 투표를 생성 할 때 구장을 최소 하나 이상 선택해야한다.")
    @WithMockPrincipalDetail(email = "test@gmail.com")
    @Test
    void createLocateVoteWhenStadiumIsNullThenThrowException() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest("연말 행사 투표", endAt, List.of());

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any()))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of()),
                    new VoteItemResponse(2L, "열정 풋살장", List.of())
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L).with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("구장 선택은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("새로운 구장 투표를 생성 할 때 구장을 중복된 구장을 선택 할 수 없다.")
    @Test
    void createLocateVoteWhenStadiumIsDuplicateThenThrowException() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        CourtChoices courtChoices1 = new CourtChoices(1L);
        CourtChoices courtChoices2 = new CourtChoices(1L);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest("연말 행사 투표", endAt, List.of(courtChoices1, courtChoices2));

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any()))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of()),
                    new VoteItemResponse(2L, "열정 풋살장", List.of())
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L).with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("중복된 구장은 포함 할 수 없습니다."))
            .andExpect(jsonPath("$.data").isEmpty());

    }

}