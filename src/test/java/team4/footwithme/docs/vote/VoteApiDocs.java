package team4.footwithme.docs.vote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.member.domain.Member;
import team4.footwithme.vote.api.VoteApi;
import team4.footwithme.vote.api.request.*;
import team4.footwithme.vote.service.VoteService;
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteCourtCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.LocalDateTime;
import java.util.List;

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

public class VoteApiDocs extends RestDocsSupport {

    private final VoteService voteService = mock(VoteService.class);

    @Override
    protected Object initController() {
        return new VoteApi(voteService);
    }


    @DisplayName("구장 투표를 등록하는 API")
    @Test
    void createStadiumVote() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        CourtChoices courtChoices1 = new CourtChoices(1L);
        CourtChoices courtChoices2 = new CourtChoices(2L);
        VoteCourtCreateRequest request = new VoteCourtCreateRequest("연말 행사 투표", endAt, List.of(courtChoices1, courtChoices2));

        given(voteService.createCourtVote(any(VoteCourtCreateServiceRequest.class), eq(1L), any(Member.class)))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of()),
                    new VoteItemResponse(2L, "열정 풋살장", List.of())
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("vote-stadium-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").description("투표 제목"),
                    fieldWithPath("endAt").description("투표 종료 시간"),
                    fieldWithPath("choices").description("투표 선택지 목록"),
                    fieldWithPath("choices[].stadiumId").description("구장 ID")
                ),
                pathParameters(
                    parameterWithName("teamId").description("팀 ID")
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
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.voteStatus").type(JsonFieldType.STRING)
                        .description("투표 상태"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].memberIds").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 투표한 회원 ID")
                )
            ));
    }

    @DisplayName("구장 투표를 조회하는 API")
    @Test
    void getVoteStadium() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        long voteId = 1L;

        given(voteService.getCourtVote(voteId))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                "진행 중",
                List.of(new VoteItemResponse(1L, "최강 풋살장", List.of(1L,2L)),
                    new VoteItemResponse(2L, "열정 풋살장", List.of(1L))
                )
            ));

        mockMvc.perform(get("/api/v1/votes/{voteId}", voteId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(document("vote-stadium-get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("voteId").description("투표 ID")
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
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.voteStatus").type(JsonFieldType.STRING)
                        .description("투표 상태"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].memberIds").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 투표한 회원 ID")
                )
            ));
    }

    @DisplayName("시간 투표를 등록하는 API")
    @Test
    void createDateVote() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(5);
        DateChoices choice1 = new DateChoices(LocalDateTime.now().plusHours(1));
        DateChoices choice2 = new DateChoices(LocalDateTime.now().plusDays(1));
        DateChoices choice3 = new DateChoices(LocalDateTime.now().plusDays(2));

        VoteDateCreateRequest request = new VoteDateCreateRequest("연말 행사 투표", endAt, List.of(choice1, choice2, choice3));

        given(voteService.createDateVote(any(VoteDateCreateServiceRequest.class), eq(1L), any(Member.class)))
            .willReturn(
                new VoteResponse(
                    1L,
                    "연말 행사 투표",
                    endAt,
                    "진행 중",
                    List.of(
                        new VoteItemResponse(1L, "2021-12-25 12:00", List.of(1L,2L)),
                        new VoteItemResponse(2L, "2021-12-26 12:00", List.of(1L)),
                        new VoteItemResponse(3L, "2021-12-27 12:00", List.of())
                    )
                )
            );

        mockMvc.perform(post("/api/v1/votes/dates/{teamId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andDo(document("vote-date-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").description("투표 제목"),
                    fieldWithPath("endAt").description("투표 종료 시간"),
                    fieldWithPath("choices").description("투표 선택지 목록"),
                    fieldWithPath("choices[].choice").description("시간")
                ),
                pathParameters(
                    parameterWithName("teamId").description("팀 ID")
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
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.voteStatus").type(JsonFieldType.STRING)
                        .description("투표 상태"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].memberIds").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 투표한 회원 ID")
                )
            ));
    }

    @DisplayName("시간 투표를 조회하는 API")
    @Test
    void getDateVote() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(5);

        long voteId = 1L;

        given(voteService.getDateVote(voteId))
            .willReturn(
                new VoteResponse(
                    1L,
                    "연말 행사 투표",
                    endAt,
                    "진행 중",
                    List.of(
                        new VoteItemResponse(1L, "2021-12-25 12:00", List.of(1L,2L)),
                        new VoteItemResponse(2L, "2021-12-26 12:00", List.of(1L)),
                        new VoteItemResponse(3L, "2021-12-27 12:00", List.of())
                    )
                )
            );

        mockMvc.perform(get("/api/v1/votes/dates/{voteId}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("vote-date-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("voteId").description("투표 ID")
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
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.voteStatus").type(JsonFieldType.STRING)
                        .description("투표 상태"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].memberIds").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 투표한 회원 ID")
                )
            ));

    }

    @DisplayName("투표ID를 이용해 투표를 삭제 할 수 있다.")
    @Test
    void deleteVote() throws Exception {
        long voteId = 1L;

        given(voteService.deleteVote(eq(voteId), any(Member.class)))
            .willReturn(voteId);

        mockMvc.perform(delete("/api/v1/votes/{voteId}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("vote-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("voteId").description("투표 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("응답 데이터 삭제한 ID")
                )
            ));

    }

    @DisplayName("투표를 수정하는 API")
    @Test
    void updateVote() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(5);

        VoteUpdateRequest request = new VoteUpdateRequest("10월 행사 투표", endAt);

        given(voteService.updateVote(any(VoteUpdateServiceRequest.class), eq(1L), any(Member.class)))
            .willReturn(
                new VoteResponse(
                    1L,
                    "10월 행사 투표",
                    endAt,
                    "진행 중",
                    List.of(
                        new VoteItemResponse(1L, "2021-12-25 12:00", List.of(1L,2L)),
                        new VoteItemResponse(2L, "2021-12-26 12:00", List.of(1L)),
                        new VoteItemResponse(3L, "2021-12-27 12:00", List.of())
                    )
                )
            );

        mockMvc.perform(put("/api/v1/votes/{voteId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andDo(document("vote-date-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").description("투표 제목"),
                    fieldWithPath("endAt").description("투표 종료 시간")
                ),
                pathParameters(
                    parameterWithName("voteId").description("투표 ID")
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
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.voteStatus").type(JsonFieldType.STRING)
                        .description("투표 상태"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].memberIds").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 투표한 회원 ID")
                )
            ));
    }

    @DisplayName("투표를 종료하는 API")
    @Test
    void closeVote() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(5);

        VoteUpdateRequest request = new VoteUpdateRequest("10월 행사 투표", endAt);

        given(voteService.closeVote(any(Long.class), any(Member.class)))
            .willReturn(
                new VoteResponse(
                    1L,
                    "10월 행사 투표",
                    endAt,
                    "마감",
                    List.of(
                        new VoteItemResponse(1L, "2021-12-25 12:00", List.of(1L,2L)),
                        new VoteItemResponse(2L, "2021-12-26 12:00", List.of(1L)),
                        new VoteItemResponse(3L, "2021-12-27 12:00", List.of())
                    )
                )
            );

        mockMvc.perform(post("/api/v1/votes/close/{voteId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andDo(document("vote-close",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("voteId").description("투표 ID")
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
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.voteStatus").type(JsonFieldType.STRING)
                        .description("투표 상태"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].memberIds").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 투표한 회원 ID")
                )
            ));
    }


}
