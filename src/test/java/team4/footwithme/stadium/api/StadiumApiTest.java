package team4.footwithme.stadium.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.jwt.JwtTokenFilter;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.stadium.api.request.StadiumSearchByLocationRequest;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StadiumApi.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class StadiumApiTest extends ApiTestSupport {


    @MockBean
    private JwtTokenUtil jwtTokenUtil;


    private StadiumsResponse stadium1;
    private StadiumsResponse stadium2;
    private StadiumDetailResponse stadiumDetail;


    @BeforeEach
    void setUp() {
        stadium1 = new StadiumsResponse(1L, "Stadium1", "seoul");
        stadium2 = new StadiumsResponse(2L, "Stadium2", "busan");
        stadiumDetail = new StadiumDetailResponse(1L, 1L, "Stadium1", "seoul", "010-1111-2222", "Description1", 37.5665, 126.9780);
    }

    @Test
    @DisplayName("GET /api/v1/stadium/stadiums - 풋살장 목록을 가져온다")
    void stadiums() throws Exception {
        Slice<StadiumsResponse> stadiums = new SliceImpl<>(Arrays.asList(stadium1, stadium2));
        when(stadiumService.getStadiumList(eq(0), eq("STADIUM"))).thenReturn(stadiums);

        mockMvc.perform(get("/api/v1/stadium/stadiums")
                        .param("page", "0")
                        .param("sort", "STADIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Stadium1"))
                .andExpect(jsonPath("$.data.content[1].name").value("Stadium2"));
    }

    @Test
    @DisplayName("GET /api/v1/stadium/stadiums/{stadiumId}/detail - 풋살장 상세 정보를 가져온다")
    void getStadiumDetailById() throws Exception {
        when(stadiumService.getStadiumDetail(eq(1L))).thenReturn(stadiumDetail);

        mockMvc.perform(get("/api/v1/stadium/stadiums/1/detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Stadium1"))
                .andExpect(jsonPath("$.data.address").value("seoul"));
    }

    @Test
    @DisplayName("GET /api/v1/stadium/stadiums/search/name - 이름으로 풋살장을 검색한다")
    void getStadiumsByName() throws Exception {
        Slice<StadiumsResponse> stadiums = new SliceImpl<>(Collections.singletonList(stadium1));
        when(stadiumService.getStadiumsByName(eq("Stadium1"), eq(0), eq("STADIUM"))).thenReturn(stadiums);

        mockMvc.perform(get("/api/v1/stadium/stadiums/search/name")
                        .param("query", "Stadium1")
                        .param("page", "0")
                        .param("sort", "STADIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Stadium1"));
    }

    @Test
    @DisplayName("GET /api/v1/stadium/stadiums/search/address - 주소로 풋살장을 검색한다")
    void getStadiumsByAddress() throws Exception {
        Slice<StadiumsResponse> stadiums = new SliceImpl<>(Collections.singletonList(stadium1));
        when(stadiumService.getStadiumsByAddress(eq("seoul"), eq(0), eq("STADIUM"))).thenReturn(stadiums);

        mockMvc.perform(get("/api/v1/stadium/stadiums/search/address")
                        .param("query", "seoul")
                        .param("page", "0")
                        .param("sort", "STADIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].address").value("seoul"));
    }

    @Test
    @DisplayName("POST /api/v1/stadium/stadiums/search/location - 위치로 풋살장을 검색한다")
    void getStadiumsByLocation() throws Exception {
        StadiumSearchByLocationRequest request = new StadiumSearchByLocationRequest(37.5665, 126.9780, 50.0);
        Slice<StadiumsResponse> stadiums = new SliceImpl<>(Collections.singletonList(stadium1));
        when(stadiumService.getStadiumsWithinDistance(any(), eq(0), eq("STADIUM"))).thenReturn(stadiums);

        mockMvc.perform(post("/api/v1/stadium/stadiums/search/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("page", "0")
                        .param("sort", "STADIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Stadium1"));
    }

    @Test
    @DisplayName("POST /api/v1/stadium/stadiums/search/location - 잘못된 위치 정보로 요청 시 BadRequest를 반환한다")
    void getStadiumsByLocation_Invalid_LatitudeRequest() throws Exception {
        StadiumSearchByLocationRequest invalidRequest = new StadiumSearchByLocationRequest(1000.0, 0.0, 50.0);

        mockMvc.perform(post("/api/v1/stadium/stadiums/search/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .param("page", "0")
                        .param("sort", "STADIUM"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("위도 값은 90도 이하이어야 합니다."));
    }

    @Test
    @DisplayName("POST /api/v1/stadium/stadiums/search/location - 잘못된 위치 정보로 요청 시 BadRequest를 반환한다")
    void getStadiumsByLocation_Invalid_LongitudeRequest() throws Exception {
        StadiumSearchByLocationRequest invalidRequest = new StadiumSearchByLocationRequest(0.0, -1000.0, 50.0);

        mockMvc.perform(post("/api/v1/stadium/stadiums/search/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .param("page", "0")
                        .param("sort", "STADIUM"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("경도 값은 -180도 이상이어야 합니다."));
    }
}
