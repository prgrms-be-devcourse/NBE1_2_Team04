package team4.footwithme.docs.stadium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.stadium.api.CourtApi;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourtApiDocs extends RestDocsSupport {

    private final CourtService courtService = mock(CourtService.class);

    @Override
    protected Object initController() {
        return new CourtApi(courtService);
    }

    @Test
    @DisplayName("전체 구장 목록 조회 API")
    void getAllCourts() throws Exception {
        CourtsResponse court1 = new CourtsResponse(1L, 1L, "Court1");
        CourtsResponse court2 = new CourtsResponse(2L, 1L, "Court2");

        Pageable pageable = PageRequest.of(0, 10);
        boolean hasNext = false;

        List<CourtsResponse> courtList = new ArrayList<>();
        courtList.add(court1);
        courtList.add(court2);

        Slice<CourtsResponse> courts = new SliceImpl<>(
                courtList,
                pageable,
                hasNext
        );

        given(courtService.getAllCourts(anyInt(), anyString()))
                .willReturn(courts);

        mockMvc.perform(get("/api/v1/court/")
                        .param("page", "0")
                        .param("sort", "COURT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("court-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").optional().description("페이지 번호"),
                                parameterWithName("sort").optional().description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("구장 목록"),
                                fieldWithPath("data.content[].courtId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.content[].stadiumId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("비페이징 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비어있는지 여부"),
                                fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("구장 ID로 구장 목록 조회 API")
    void getCourtsByStadiumId() throws Exception {
        Long stadiumId = 1L;
        CourtsResponse court1 = new CourtsResponse(1L, stadiumId, "Court1");
        CourtsResponse court2 = new CourtsResponse(2L, stadiumId, "Court2");

        Pageable pageable = PageRequest.of(0, 10);
        boolean hasNext = false;

        List<CourtsResponse> courtList = new ArrayList<>();
        courtList.add(court1);
        courtList.add(court2);

        Slice<CourtsResponse> courts = new SliceImpl<>(
                courtList,
                pageable,
                hasNext
        );

        given(courtService.getCourtsByStadiumId(eq(stadiumId), anyInt(), anyString()))
                .willReturn(courts);

        mockMvc.perform(get("/api/v1/court/{stadiumId}/courts", stadiumId)
                        .param("page", "0")
                        .param("sort", "COURT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("court-get-by-stadium",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stadiumId").description("구장 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").optional().description("페이지 번호"),
                                parameterWithName("sort").optional().description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("구장 목록"),
                                fieldWithPath("data.content[].courtId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.content[].stadiumId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("비페이징 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비어있는지 여부"),
                                fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("구장 상세 정보 조회 API")
    void getCourtDetailById() throws Exception {
        Long courtId = 1L;
        CourtDetailResponse courtDetail = new CourtDetailResponse(
                courtId, 1L, "Court1", "GreatCourt", new BigDecimal(10000)
        );

        given(courtService.getCourtByCourtId(eq(courtId)))
                .willReturn(courtDetail);

        mockMvc.perform(get("/api/v1/court/{courtId}/detail", courtId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("court-get-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("courtId").description("구장 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.courtId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).description("구장 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("구장 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("구장 설명"),
                                fieldWithPath("data.price_per_hour").type(JsonFieldType.NUMBER).description("시간당 가격")
                        )
                ));
    }
}
