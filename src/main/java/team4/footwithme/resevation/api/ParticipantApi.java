package team4.footwithme.resevation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.api.request.ParticipantUpdateRequest;
import team4.footwithme.resevation.service.ParticipantService;
import team4.footwithme.resevation.service.response.ParticipantResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantApi {
    private final ParticipantService participantService;

    @PostMapping("/mercenary/{mercenaryId}")
    public ApiResponse<ParticipantResponse> applyMercenary(@PathVariable Long mercenaryId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(participantService.createMercenaryParticipant(mercenaryId, principalDetails.getMember()));
    }

    @PostMapping("/reservation/join/{reservationId}")
    public ApiResponse<ParticipantResponse> joinReservation(@PathVariable Long reservationId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(participantService.createParticipant(reservationId, principalDetails.getMember()));
    }

    @DeleteMapping("/reservation/leave/{reservationId}")
    public ApiResponse<Long> leaveReservation(@PathVariable Long reservationId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(participantService.deleteParticipant(reservationId, principalDetails.getMember()));
    }

    @PutMapping
    public ApiResponse<ParticipantResponse> updateParticipant(@RequestBody @Valid ParticipantUpdateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(participantService.updateMercenaryParticipant(request.toServiceResponse(), principalDetails.getMember()));
    }

    @GetMapping("/accept/{reservationId}")
    public ApiResponse<List<ParticipantResponse>> getAcceptParticipants(@PathVariable Long reservationId) {
        return ApiResponse.ok(participantService.getAcceptParticipants(reservationId));
    }

    @GetMapping("/pending/{reservationId}")
    public ApiResponse<List<ParticipantResponse>> getPendingParticipants(@PathVariable Long reservationId) {
        return ApiResponse.ok(participantService.getParticipantsMercenary(reservationId));
    }

    @GetMapping("/all/{reservationId}")
    public ApiResponse<List<ParticipantResponse>> getAllParticipants(@PathVariable Long reservationId) {
        return ApiResponse.ok(participantService.getParticipants(reservationId));
    }
}
