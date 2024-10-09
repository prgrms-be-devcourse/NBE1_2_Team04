package team4.footwithme.docs.mercenary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.resevation.api.MWMercenaryApi;
import team4.footwithme.resevation.api.request.MWMercenaryRequest;
import team4.footwithme.resevation.service.MercenaryServiceImpl;
import team4.footwithme.resevation.service.request.MWMercenaryServiceRequest;
import team4.footwithme.resevation.service.response.MWMercenaryResponse;
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

public class MWMercenaryApiDocs extends RestDocsSupport {
    private final MercenaryServiceImpl mercenaryService = mock(MercenaryServiceImpl.class);

    @Override
    protected Object initController() {
        return new MWMercenaryApi(mercenaryService);
    }

    //용병 게시판을 추가할 수 있다.
    @DisplayName("용병 게시판을 추가 API")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void createMercenary() throws Exception {

        MWMercenaryRequest request = new MWMercenaryRequest(
                1L,
                "용병 구합니다"
        );

        MWMercenaryResponse response = new MWMercenaryResponse(
                1L,
                1L,
                "(10/08 09:00)(스타 구장) 용병 구합니다"
        );

        given(mercenaryService.createMercenary(any(MWMercenaryServiceRequest.class),any()))
                .willReturn(response);

        mockMvc.perform(post("/api/v1/mercenary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("mercenary-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("reservationId").description("예약 ID"),
                                fieldWithPath("description").description("용병 게시판 설명")
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
                                fieldWithPath("data.mercenaryId").type(JsonFieldType.NUMBER)
                                        .description("용병 게시판 ID"),
                                fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER)
                                        .description("예약 ID"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                        .description("용병 게시판 설명")
                        )
                ));
    }

    //용병 게시판을 삭제할 수 있다.
    @DisplayName("용병 게시판을 삭제할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void deleteMercenary() throws Exception {
        Long mercenaryId = 1L;

        given(mercenaryService.deleteMercenary(any(Long.class),any()))
                .willReturn(mercenaryId);

        mockMvc.perform(delete("/api/v1/mercenary/{mercenaryId}", mercenaryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mercenary-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("mercenaryId").description("삭제 할 용병 게시판 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("삭제한 용병 게시판 ID"))
                ));
    }

    //단일 용병 게시판을 조회할 수 있다.
    @DisplayName("단일 용병 게시판을 조회할 수 있다.")
    @Test
    void getMercenary() throws Exception {
        Long mercenaryId = 1L;

        MWMercenaryResponse response = new MWMercenaryResponse(
                1L,
                1L,
                "(10/08 09:00)(스타 구장) 용병 구합니다"
        );

        given(mercenaryService.getMercenary(any(Long.class)))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/mercenary/{mercenaryId}", mercenaryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mercenary-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("mercenaryId").description("조회 할 용병 게시판 ID")
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
                                fieldWithPath("data.mercenaryId").type(JsonFieldType.NUMBER)
                                        .description("용병 게시판 ID"),
                                fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER)
                                        .description("예약 ID"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                        .description("용병 게시판 설명"))));
    }

    //전체 용병 게시판을 조회할 수 있다.
    @DisplayName("단일 용병 게시판을 조회할 수 있다.")
    @Test
    void getMercenaryPage() throws Exception {
        int page = 1;
        int size = 1;

        MWMercenaryResponse response = new MWMercenaryResponse(
                1L,
                1L,
                "(10/08 09:00)(스타 구장) 용병 구합니다"
        );

        List<MWMercenaryResponse> responseList = new ArrayList<>();

        responseList.add(response);

        PageRequest pageRequest = PageRequest.of(page-1, size);

        Page<MWMercenaryResponse> responsePage = new PageImpl<>(responseList, pageRequest, 1);

        given(mercenaryService.getMercenaries(any(PageRequest.class)))
                .willReturn(responsePage);

        mockMvc.perform(get("/api/v1/mercenary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andDo(document("mercenary-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.queryParameters(
                                parameterWithName("page").description("페이지 넘버"),
                                parameterWithName("size").description("페이지 사이즈")
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
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                        .description("컨텐츠 리스트"),
                                fieldWithPath("data.content[].mercenaryId").type(JsonFieldType.NUMBER)
                                        .description("용병 게시판 ID"),
                                fieldWithPath("data.content[].reservationId").type(JsonFieldType.NUMBER)
                                        .description("예약 ID"),
                                fieldWithPath("data.content[].description").type(JsonFieldType.STRING)
                                        .description("용병 게시판 설명"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT)
                                        .description("Pageable 정보"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("Pageable 페이지 넘버"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("Pageable 페이지 사이즈"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("Pageable 정렬 정보"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 정렬 존재 확인"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 비정렬 확인"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 정렬 확인"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("Pageable 오프셋"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 페이징 확인"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 비페이징 확인"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("총 페이지 사이즈"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("총 컨텐츠 갯수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 사이즈"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                        .description("페이지 넘버"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 정렬 존재 확인"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 비정렬 확인"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 정렬 확인"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 첫번째 확인"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 마지막 확인"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("페이지 컨텐츠 갯수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 존재 확인"))));
    }
}
