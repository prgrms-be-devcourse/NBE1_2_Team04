package team4.footwithme.resevation.api

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.resevation.api.request.ParticipantUpdateRequest
import team4.footwithme.resevation.service.ParticipantService
import team4.footwithme.resevation.service.response.ParticipantResponse

@RestController
@RequestMapping("/api/v1/participant")
class ParticipantApi(private val participantService: ParticipantService) {
    @PostMapping("/mercenary/{mercenaryId}")
    fun applyMercenary(
        @PathVariable mercenaryId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<ParticipantResponse?> {
        return ApiResponse.Companion.created<ParticipantResponse?>(
            participantService.createMercenaryParticipant(
                mercenaryId,
                principalDetails.member
            )
        )
    }

    @PostMapping("/reservation/join/{reservationId}")
    fun joinReservation(
        @PathVariable reservationId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<ParticipantResponse?> {
        return ApiResponse.Companion.created<ParticipantResponse?>(
            participantService.createParticipant(
                reservationId,
                principalDetails.member
            )
        )
    }

    @DeleteMapping("/reservation/leave/{reservationId}")
    fun leaveReservation(
        @PathVariable reservationId: Long,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Long?> {
        return ApiResponse.Companion.ok<Long?>(
            participantService.deleteParticipant(
                reservationId,
                principalDetails.member
            )
        )
    }

    @PutMapping
    fun updateParticipant(
        @RequestBody request: @Valid ParticipantUpdateRequest?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<ParticipantResponse?> {
        return ApiResponse.Companion.ok<ParticipantResponse?>(
            participantService.updateMercenaryParticipant(
                request!!.toServiceResponse(),
                principalDetails.member
            )
        )
    }

    @GetMapping("/accept/{reservationId}")
    fun getAcceptParticipants(@PathVariable reservationId: Long?): ApiResponse<List<ParticipantResponse>?> {
        return ApiResponse.Companion.ok<List<ParticipantResponse>?>(
            participantService.getAcceptParticipants(
                reservationId
            )
        )
    }

    @GetMapping("/pending/{reservationId}")
    fun getPendingParticipants(@PathVariable reservationId: Long?): ApiResponse<List<ParticipantResponse>?> {
        return ApiResponse.Companion.ok<List<ParticipantResponse>?>(
            participantService.getParticipantsMercenary(
                reservationId
            )
        )
    }

    @GetMapping("/all/{reservationId}")
    fun getAllParticipants(@PathVariable reservationId: Long?): ApiResponse<List<ParticipantResponse>?> {
        return ApiResponse.Companion.ok<List<ParticipantResponse>?>(participantService.getParticipants(reservationId))
    }
}
