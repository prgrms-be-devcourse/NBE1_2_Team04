package team4.footwithme.docs.stadium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.stadium.api.StadiumApi;
import team4.footwithme.stadium.api.request.StadiumSearchByLocationRequest;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StadiumApiDocs extends RestDocsSupport {

    private final StadiumService stadiumService = mock(StadiumService.class);

    @Override
    protected Object initController() {
        return new StadiumApi(stadiumService);
    }

    @Test
    @DisplayName("풋살장 목록 조회 API")
    void getStadiums() throws Exception {
        StadiumsResponse stadium1 = new StadiumsResponse(1L, "Stadium1", "Seoul");
        StadiumsResponse stadium2 = new StadiumsResponse(2L, "Stadium2", "Busan");

        Pageable pageable = PageRequest.of(0, 10);
        boolean hasNext = false;

        List<StadiumsResponse> stadiumList = new ArrayList<>();
        stadiumList.add(stadium1);
        stadiumList.add(stadium2);

        Slice<StadiumsResponse> stadiums = new SliceImpl<>(
                stadiumList,
                pageable,
                hasNext
        );

        given(stadiumService.getStadiumList(anyInt(), anyString()))
                .willReturn(stadiums);

        mockMvc.perform(get("/api/v1/stadium/stadiums")
                        .param("page", "0")
                        .param("sort", "STADIUM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stadium-get-list",
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
                                subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("풋살장 목록"),
                                fieldWithPath("data.content[].stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
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

    @Test
    @DisplayName("풋살장 상세 정보 조회 API")
    void getStadiumDetail() throws Exception {
        Long stadiumId = 1L;
        StadiumDetailResponse stadiumDetail = new StadiumDetailResponse(
                stadiumId, 1L, "Stadium1", "Seoul", "010-1234-5678",
                "Nice stadium", 37.5665, 126.9780
        );

        given(stadiumService.getStadiumDetail(eq(stadiumId)))
                .willReturn(stadiumDetail);

        mockMvc.perform(get("/api/v1/stadium/stadiums/{stadiumId}/detail", stadiumId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stadium-get-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stadiumId").description("풋살장 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("설명"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("경도")
                        )
                ));
    }

    @Test
    @DisplayName("풋살장 이름으로 검색 API")
    void searchStadiumsByName() throws Exception {
        String query = "Stadium1";
        StadiumsResponse stadium = new StadiumsResponse(1L, "Stadium1", "Seoul");

        Pageable pageable = PageRequest.of(0, 10);
        boolean hasNext = false;

        List<StadiumsResponse> stadiumList = new ArrayList<>();
        stadiumList.add(stadium);

        Slice<StadiumsResponse> stadiums = new SliceImpl<>(
                stadiumList,
                pageable,
                hasNext
        );

        given(stadiumService.getStadiumsByName(eq(query), anyInt(), anyString()))
                .willReturn(stadiums);

        mockMvc.perform(get("/api/v1/stadium/stadiums/search/name")
                        .param("query", query)
                        .param("page", "0")
                        .param("sort", "STADIUM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stadium-search-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("query").description("검색할 풋살장 이름"),
                                parameterWithName("page").optional().description("페이지 번호"),
                                parameterWithName("sort").optional().description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("풋살장 목록"),
                                fieldWithPath("data.content[].stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
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

    @Test
    @DisplayName("풋살장 주소로 검색 API")
    void searchStadiumsByAddress() throws Exception {
        String query = "Seoul";
        StadiumsResponse stadium = new StadiumsResponse(1L, "Stadium1", "Seoul");

        Pageable pageable = PageRequest.of(0, 10);
        boolean hasNext = false;

        List<StadiumsResponse> stadiumList = new ArrayList<>();
        stadiumList.add(stadium);

        Slice<StadiumsResponse> stadiums = new SliceImpl<>(
                stadiumList,
                pageable,
                hasNext
        );

        given(stadiumService.getStadiumsByAddress(eq(query), anyInt(), anyString()))
                .willReturn(stadiums);

        mockMvc.perform(get("/api/v1/stadium/stadiums/search/address")
                        .param("query", query)
                        .param("page", "0")
                        .param("sort", "STADIUM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stadium-search-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("query").description("검색할 주소"),
                                parameterWithName("page").optional().description("페이지 번호"),
                                parameterWithName("sort").optional().description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("풋살장 목록"),
                                fieldWithPath("data.content[].stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
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

    @Test
    @DisplayName("위치로 풋살장 검색 API")
    void searchStadiumsByLocation() throws Exception {
        StadiumSearchByLocationRequest request = new StadiumSearchByLocationRequest(
                37.5665, 126.9780, 50.0
        );
        StadiumSearchByLocationServiceRequest serviceRequest = request.toServiceRequest();

        StadiumsResponse stadium = new StadiumsResponse(1L, "Stadium1", "Seoul");

        Pageable pageable = PageRequest.of(0, 10);
        boolean hasNext = false;

        List<StadiumsResponse> stadiumList = new ArrayList<>();
        stadiumList.add(stadium);

        Slice<StadiumsResponse> stadiums = new SliceImpl<>(
                stadiumList,
                pageable,
                hasNext
        );

        given(stadiumService.getStadiumsWithinDistance(eq(serviceRequest), anyInt(), anyString()))
                .willReturn(stadiums);

        mockMvc.perform(post("/api/v1/stadium/stadiums/search/location")
                        .param("page", "0")
                        .param("sort", "STADIUM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("stadium-search-location",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").optional().description("페이지 번호"),
                                parameterWithName("sort").optional().description("정렬 기준")
                        ),
                        requestFields(
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리 (km)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("풋살장 목록"),
                                fieldWithPath("data.content[].stadiumId").type(JsonFieldType.NUMBER).description("풋살장 ID"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("풋살장 이름"),
                                fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("풋살장 주소"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
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
