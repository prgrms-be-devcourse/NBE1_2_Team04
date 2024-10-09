package team4.footwithme.docs.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.resevation.api.GameApi;
import team4.footwithme.resevation.api.request.GameRegisterRequest;
import team4.footwithme.resevation.api.request.GameStatusUpdateRequest;
import team4.footwithme.resevation.service.GameService;
import team4.footwithme.resevation.service.response.GameDetailResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameApiDocs extends RestDocsSupport {

    private final GameService gameService = mock(GameService.class);

    @Override
    protected Object initController() {
        return new GameApi(gameService);
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 등록 API")
    void registerGame() throws Exception {
        GameRegisterRequest request = new GameRegisterRequest(1L, 2L);
        GameDetailResponse response = new GameDetailResponse(1L, 1L, 2L, team4.footwithme.resevation.domain.GameStatus.READY);

        given(gameService.registerGame(any(), any())).willReturn(response);

        mockMvc.perform(post("/api/v1/game/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("game-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("firstReservationId").type(JsonFieldType.NUMBER).description("신청 예약 아이디"),
                                fieldWithPath("secondReservationId").type(JsonFieldType.NUMBER).description("신청 받는 예약 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.gameId").type(JsonFieldType.NUMBER).description("게임 ID"),
                                fieldWithPath("data.firstReservationId").type(JsonFieldType.NUMBER).description("첫 번째 예약 아이디"),
                                fieldWithPath("data.secondReservationId").type(JsonFieldType.NUMBER).description("두 번째 예약 아이디"),
                                fieldWithPath("data.gameStatus").type(JsonFieldType.STRING).description("게임 상태")
                        )
                ));
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 상태 수정 API")
    void updateGameStatus() throws Exception {
        GameStatusUpdateRequest request = new GameStatusUpdateRequest(1L, team4.footwithme.resevation.domain.GameStatus.READY);

        given(gameService.updateGameStatus(any(), any())).willReturn("게임 상태가 업데이트되었습니다.");

        mockMvc.perform(put("/api/v1/game/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("game-status-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("gameId").type(JsonFieldType.NUMBER).description("게임 ID"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("게임 상태 (READY, IGNORE 등)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @WithMockPrincipalDetail(email = "user@example.com", role = team4.footwithme.member.domain.MemberRole.USER)
    @DisplayName("게임 목록 조회 API")
    void getPendingGames() throws Exception {
        // Mock 데이터 설정
        GameDetailResponse game1 = new GameDetailResponse(1L, 1L, 2L, team4.footwithme.resevation.domain.GameStatus.READY);
        GameDetailResponse game2 = new GameDetailResponse(2L, 3L, 4L, team4.footwithme.resevation.domain.GameStatus.IGNORE);

        List<GameDetailResponse> gameList = List.of(game1, game2);
        Pageable pageable = PageRequest.of(0, 10);
        Slice<GameDetailResponse> games = new SliceImpl<>(gameList, pageable, false);

        // Mock 서비스 메서드 설정
        given(gameService.findPendingGames(any(), eq(1L), anyInt())).willReturn(games);

        // 테스트 수행
        mockMvc.perform(get("/api/v1/game/game")
                        .param("page", "0")
                        .param("reservationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("game-get-pending",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").optional().description("페이지 번호 (기본값은 0)"),
                                parameterWithName("reservationId").description("예약 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("게임 목록"),
                                fieldWithPath("data.content[].gameId").type(JsonFieldType.NUMBER).description("게임 ID"),
                                fieldWithPath("data.content[].firstReservationId").type(JsonFieldType.NUMBER).description("첫 번째 예약 아이디"),
                                fieldWithPath("data.content[].secondReservationId").type(JsonFieldType.NUMBER).description("두 번째 예약 아이디"),
                                fieldWithPath("data.content[].gameStatus").type(JsonFieldType.STRING).description("게임 상태"),
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
}