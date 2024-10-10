package team4.footwithme.resevation.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.service.response.ReservationsResponse;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ReservationApiTest extends ApiTestSupport {


    private ReservationsResponse reservation;

    @BeforeEach
    void setUp() {
        reservation = new ReservationsResponse(1L, 1L, 1L, 1L, LocalDateTime.parse("2024-10-01T10:00"), ParticipantGender.MALE);
        reservation = new ReservationsResponse(2L, 1L, 2L, 2L, LocalDateTime.parse("2024-10-01T10:00"), ParticipantGender.MALE);
    }

    @Test
    @DisplayName("GET /api/v1/reservation/ready - 준비 중인 예약 목록을 가져온다")
    void getReadyReservations() throws Exception {
        Slice<ReservationsResponse> reservations = new SliceImpl<>(Collections.singletonList(reservation));
        when(reservationService.findReadyReservations(eq(1L), eq(0))).thenReturn(reservations);

        mockMvc.perform(get("/api/v1/reservation/ready")
                .param("reservationId", "1")
                .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].reservationId").value(2L))
            .andExpect(jsonPath("$.data.content[0].courtId").value(1L))
            .andExpect(jsonPath("$.data.content[0].memberId").value(2L))
            .andExpect(jsonPath("$.data.content[0].teamId").value(2L))
            .andExpect(jsonPath("$.data.content[0].gender").value("MALE"));
    }
}