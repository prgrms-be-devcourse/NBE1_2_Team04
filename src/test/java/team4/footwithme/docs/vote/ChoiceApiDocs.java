package team4.footwithme.docs.vote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.vote.api.ChoiceApi;
import team4.footwithme.vote.api.request.ChoiceCreateRequest;
import team4.footwithme.vote.service.VoteService;
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChoiceApiDocs extends RestDocsSupport {

    private final VoteService voteService = mock(VoteService.class);

    @Override
    protected Object initController() {
        return new ChoiceApi(voteService);
    }

    @DisplayName("투표 상세 항목에 중복 투표를 진행 할 수 있다.")
    @Test
    void createVoteItemChoice() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        long voteId = 1L;

        ChoiceCreateRequest request = new ChoiceCreateRequest(List.of(1L, 2L));

        given(voteService.createChoice(any(ChoiceCreateServiceRequest.class),eq(voteId),any(String.class)))
            .willReturn(new VoteResponse(
                voteId,
                "연말 행사 투표",
                endAt,
                List.of(
                    new VoteItemResponse(1L, "2021-12-25 12:00", 1L),
                    new VoteItemResponse(2L, "2021-12-26 12:00", 1L),
                    new VoteItemResponse(3L, "2021-12-27 12:00", 0L)
                )
            ));

        mockMvc.perform(post("/api/v1/choice/{voteId}", voteId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("choice-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("voteItemIds").description("투표 선택지 ID")
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
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].voteCount").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 투표 수")
                )
            ));

    }

    @DisplayName("투표 상세 항목에 투표를 취소 할 수 있다. 단 모든 투표 내용이 취소된다.")
    @Test
    void cancelVoteItemChoice() throws Exception {
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        long voteId = 1L;

        given(voteService.deleteChoice(eq(voteId),any(String.class)))
            .willReturn(new VoteResponse(
                voteId,
                "연말 행사 투표",
                endAt,
                List.of(
                    new VoteItemResponse(1L, "2021-12-25 12:00", 0L),
                    new VoteItemResponse(2L, "2021-12-26 12:00", 0L),
                    new VoteItemResponse(3L, "2021-12-27 12:00", 0L)
                )
            ));

        mockMvc.perform(delete("/api/v1/choice/{voteId}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("choice-delete",
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
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].voteCount").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 투표 수")
                )
            ));

    }


}
