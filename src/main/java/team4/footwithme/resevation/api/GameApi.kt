package team4.footwithme.resevation.api

import jakarta.validation.Valid
import org.springframework.data.domain.Slice
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.resevation.api.request.GameRegisterRequest
import team4.footwithme.resevation.api.request.GameStatusUpdateRequest
import team4.footwithme.resevation.service.GameService
import team4.footwithme.resevation.service.response.GameDetailResponse

@RestController
@RequestMapping("/api/v1/game")
class GameApi(private val gameService: GameService) {
    @PostMapping("/register")
    fun registerGame(
        @AuthenticationPrincipal currentUser: PrincipalDetails,
        @RequestBody request: @Valid GameRegisterRequest?
    ): ApiResponse<GameDetailResponse?> {
        return ApiResponse.Companion.created<GameDetailResponse?>(
            gameService.registerGame(
                currentUser.member,
                request!!.toServiceRequest()
            )
        )
    }

    @GetMapping("/game")
    fun getPendingGames(
        @AuthenticationPrincipal currentUser: PrincipalDetails,
        @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam reservationId: Long?
    ): ApiResponse<Slice<GameDetailResponse>?> {
        return ApiResponse.Companion.ok<Slice<GameDetailResponse>?>(
            gameService.findPendingGames(
                currentUser.member,
                reservationId,
                page
            )
        )
    }

    @PutMapping("/status")
    fun updateGameStatus(
        @AuthenticationPrincipal currentUser: PrincipalDetails,
        @RequestBody request: @Valid GameStatusUpdateRequest?
    ): ApiResponse<String?> {
        return ApiResponse.Companion.ok<String?>(
            gameService.updateGameStatus(
                currentUser.member,
                request!!.toServiceRequest()
            )
        )
    }
}
