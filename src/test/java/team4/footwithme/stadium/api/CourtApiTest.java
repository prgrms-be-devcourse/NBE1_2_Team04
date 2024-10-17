package team4.footwithme.stadium.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.jwt.JwtTokenFilter;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CourtApi.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class CourtApiTest extends ApiTestSupport {

    private CourtsResponse court1;
    private CourtsResponse court2;
    private CourtDetailResponse courtDetail;

    @BeforeEach
    void setUp() {
        court1 = new CourtsResponse(1L, 1L, "Court1");
        court2 = new CourtsResponse(2L, 1L, "Court2");
        courtDetail = new CourtDetailResponse(1L, 1L, "Court1", "Description1", new BigDecimal("10000"));
    }

    @Test
    @DisplayName("GET /api/v1/court/ - 모든 구장을 가져온다")
    void getAllCourts() throws Exception {
        Slice<CourtsResponse> courts = new SliceImpl<>(Arrays.asList(court1, court2));
        when(courtService.getAllCourts(eq(0), eq("COURT"))).thenReturn(courts);

        mockMvc.perform(get("/api/v1/court/")
                .param("page", "0")
                .param("sort", "COURT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].name").value("Court1"))
            .andExpect(jsonPath("$.data.content[1].name").value("Court2"));
    }

    @Test
    @DisplayName("GET /api/v1/court/{stadiumId}/courts - 특정 풋살장의 구장을 가져온다")
    void getCourtsByStadiumId() throws Exception {
        Slice<CourtsResponse> courts = new SliceImpl<>(Collections.singletonList(court1));
        when(courtService.getCourtsByStadiumId(eq(1L), eq(0), eq("COURT"))).thenReturn(courts);

        mockMvc.perform(get("/api/v1/court/1/courts")
                .param("page", "0")
                .param("sort", "COURT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].name").value("Court1"));
    }

    @Test
    @DisplayName("GET /api/v1/court/{stadiumId}/courts - 잘못된 풋살장의 ID로 요청 시 BadRequest를 반환한다")
    void getCourtsByInvalidStadiumId() throws Exception {
        Long invalidStadiumId = -1L;

        when(courtService.getCourtsByStadiumId(eq(invalidStadiumId), eq(0), eq("COURT")))
            .thenThrow(new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_FOUND.text));

        mockMvc.perform(get("/api/v1/court/{stadiumId}/courts", invalidStadiumId)
                .param("page", "0")
                .param("sort", "COURT"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ExceptionMessage.STADIUM_NOT_FOUND.text));
    }

    @Test
    @DisplayName("GET /api/v1/court/{courtId}/detail - 구장 상세 정보를 가져온다")
    void getCourtDetailById() throws Exception {
        when(courtService.getCourtByCourtId(eq(1L))).thenReturn(courtDetail);

        mockMvc.perform(get("/api/v1/court/1/detail"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("Court1"))
            .andExpect(jsonPath("$.data.description").value("Description1"));
    }

    @Test
    @DisplayName("GET /api/v1/court/{courtId}/detail - 잘못된 구장의 Id로 요청 시 BadRequest를 반환한다")
    void getCourtDetailByInvalidId() throws Exception {
        Long invalidCourtId = -1L;

        when(courtService.getCourtByCourtId(eq(invalidCourtId)))
            .thenThrow(new IllegalArgumentException(ExceptionMessage.COURT_NOT_FOUND.text));

        mockMvc.perform(get("/api/v1/court/{courtId}/detail", invalidCourtId))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ExceptionMessage.COURT_NOT_FOUND.text));
    }
}
