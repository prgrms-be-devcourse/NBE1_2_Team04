package team4.footwithme.docs.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.resevation.api.JKReservationApi;
import team4.footwithme.resevation.service.JKReservationService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JKReservationApiDocs extends RestDocsSupport {
    private final JKReservationService reservationService = mock(JKReservationService.class);

    @Override
    protected Object initController() {
        return new JKReservationApi(reservationService);
    }

    @DisplayName("예약 취소하는 API")
    @Test
    void deleteReservation() throws Exception {
        //given
        Long reservationId = 1L;

        //when
        given(reservationService.deleteReservation(eq(reservationId), any()))
                .willReturn(reservationId);

        //then
        mockMvc.perform(delete("/api/v1/reservation/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN"))
                .andExpect(status().isOk())
                .andDo(document("reservation-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("삭제된 예약 ID")
                        )
                ));
    }
}
