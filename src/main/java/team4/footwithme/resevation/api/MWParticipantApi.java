package team4.footwithme.resevation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.api.request.MWParticipantUpdateRequest;
import team4.footwithme.resevation.service.MWParticipantServiceImpl;
import team4.footwithme.resevation.service.response.MWParticipantResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/participant")
public class MWParticipantApi {
    private final MWParticipantServiceImpl participantService;

    @PostMapping("/mercenary/{mercenaryId}")
    public ApiResponse<MWParticipantResponse> applyMercenary(@PathVariable Long mercenaryId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(participantService.createMercenaryParticipant(mercenaryId, principalDetails.getMember()));
    }

    @PostMapping("/reservation/join/{reservationId}")
    public ApiResponse<MWParticipantResponse> joinReservation(@PathVariable Long reservationId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(participantService.createParticipant(reservationId, principalDetails.getMember()));
    }

    @DeleteMapping("/reservation/leave/{reservationId}")
    public ApiResponse<Long> leaveReservation(@PathVariable Long reservationId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(participantService.deleteParticipant(reservationId, principalDetails.getMember()));
    }

    @PutMapping
    public ApiResponse<MWParticipantResponse> updateParticipant(@RequestBody @Valid MWParticipantUpdateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(participantService.updateMercenaryParticipant(request.toServiceResponse(), principalDetails.getMember()));
    }

    @GetMapping("/accept/{reservationId}")
    public ApiResponse<List<MWParticipantResponse>> getAcceptParticipants(@PathVariable Long reservationId) {
        return ApiResponse.ok(participantService.getAcceptParticipants(reservationId));
    }

    @GetMapping("/pending/{reservationId}")
    public ApiResponse<List<MWParticipantResponse>> getPendingParticipants(@PathVariable Long reservationId) {
        return ApiResponse.ok(participantService.getParticipantsMercenary(reservationId));
    }

    @GetMapping("/all/{reservationId}")
    public ApiResponse<List<MWParticipantResponse>> getAllParticipants(@PathVariable Long reservationId) {
        return ApiResponse.ok(participantService.getParticipants(reservationId));
    }

}
