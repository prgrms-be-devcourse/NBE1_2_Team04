package team4.footwithme.mercenary.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.api.request.MercenaryRequest;
import team4.footwithme.resevation.service.request.MercenaryServiceRequest;
import team4.footwithme.resevation.service.response.MercenaryResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MercenaryApiTest extends ApiTestSupport {

    //용병 게시판을 추가할 수 있다.
    @DisplayName("용병 게시판을 추가할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void createMercenary() throws Exception {

        MercenaryRequest request = new MercenaryRequest(
            1L,
            "용병 구합니다"
        );

        MercenaryResponse response = new MercenaryResponse(
            1L,
            1L,
            "(10/08 09:00)(스타 구장) 용병 구합니다"
        );

        given(mercenaryService.createMercenary(any(MercenaryServiceRequest.class), any(Member.class)))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/mercenary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.mercenaryId").value(1L))
            .andExpect(jsonPath("$.data.reservationId").value(1L))
            .andExpect(jsonPath("$.data.description").value("(10/08 09:00)(스타 구장) 용병 구합니다"));
    }

    //용병 게시판을 삭제할 수 있다.
    @DisplayName("용병 게시판을 삭제할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void deleteMercenary() throws Exception {
        Long mercenaryId = 1L;

        given(mercenaryService.deleteMercenary(any(Long.class), any(Member.class)))
            .willReturn(mercenaryId);

        mockMvc.perform(delete("/api/v1/mercenary/{mercenaryId}", mercenaryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value(1L));
    }

    //단일 용병 게시판을 조회할 수 있다.
    @DisplayName("단일 용병 게시판을 조회할 수 있다.")
    @Test
    void getMercenary() throws Exception {
        Long mercenaryId = 1L;

        MercenaryResponse response = new MercenaryResponse(
            1L,
            1L,
            "(10/08 09:00)(스타 구장) 용병 구합니다"
        );

        given(mercenaryService.getMercenary(any(Long.class)))
            .willReturn(response);

        mockMvc.perform(get("/api/v1/mercenary/{mercenaryId}", mercenaryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.mercenaryId").value(1L))
            .andExpect(jsonPath("$.data.reservationId").value(1L))
            .andExpect(jsonPath("$.data.description").value("(10/08 09:00)(스타 구장) 용병 구합니다"));
    }

    //전체 용병 게시판을 조회할 수 있다.
    @DisplayName("단일 용병 게시판을 조회할 수 있다.")
    @Test
    void getMercenaryPage() throws Exception {
        int page = 1;
        int size = 1;

        MercenaryResponse response = new MercenaryResponse(
            1L,
            1L,
            "(10/08 09:00)(스타 구장) 용병 구합니다"
        );

        List<MercenaryResponse> responseList = new ArrayList<>();

        responseList.add(response);

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<MercenaryResponse> responsePage = new PageImpl<>(responseList, pageRequest, 1);

        given(mercenaryService.getMercenaries(any(PageRequest.class)))
            .willReturn(responsePage);

        mockMvc.perform(get("/api/v1/mercenary")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content[0].mercenaryId").value(1L))
            .andExpect(jsonPath("$.data.content[0].reservationId").value(1L))
            .andExpect(jsonPath("$.data.content[0].description").value("(10/08 09:00)(스타 구장) 용병 구합니다"))
            .andExpect(jsonPath("$.data.pageable").isMap())
            .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
            .andExpect(jsonPath("$.data.pageable.pageSize").value(1))
            .andExpect(jsonPath("$.data.pageable.sort").isMap())
            .andExpect(jsonPath("$.data.pageable.sort.empty").value(true))
            .andExpect(jsonPath("$.data.pageable.sort.unsorted").value(true))
            .andExpect(jsonPath("$.data.pageable.sort.sorted").value(false))
            .andExpect(jsonPath("$.data.pageable.offset").value(0))
            .andExpect(jsonPath("$.data.pageable.paged").value(true))
            .andExpect(jsonPath("$.data.pageable.unpaged").value(false))
            .andExpect(jsonPath("$.data.size").value(1))
            .andExpect(jsonPath("$.data.number").value(0))
            .andExpect(jsonPath("$.data.sort").isMap())
            .andExpect(jsonPath("$.data.sort.empty").value(true))
            .andExpect(jsonPath("$.data.sort.unsorted").value(true))
            .andExpect(jsonPath("$.data.sort.sorted").value(false))
            .andExpect(jsonPath("$.data.first").value(true))
            .andExpect(jsonPath("$.data.last").value(true))
            .andExpect(jsonPath("$.data.numberOfElements").value(1))
            .andExpect(jsonPath("$.data.empty").value(false));

    }
}
