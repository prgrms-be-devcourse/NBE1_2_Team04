package team4.footwithme.resevation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.api.request.GameRegisterRequest;
import team4.footwithme.resevation.api.request.GameStatusUpdateRequest;
import team4.footwithme.resevation.service.GameService;
import team4.footwithme.resevation.service.response.GameDetailResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/game")
public class GameApi {

    private final GameService gameService;

    @PostMapping("/register")
    public ApiResponse<GameDetailResponse> registerGame(
        @AuthenticationPrincipal PrincipalDetails currentUser,
        @Valid @RequestBody GameRegisterRequest request) {
        return ApiResponse.created(gameService.registerGame(currentUser.getMember(), request.toServiceRequest()));
    }

    @GetMapping("/game")
    public ApiResponse<Slice<GameDetailResponse>> getPendingGames(
        @AuthenticationPrincipal PrincipalDetails currentUser,
        @RequestParam(defaultValue = "0", required = false) Integer page,
        @RequestParam Long reservationId) {
        return ApiResponse.ok(gameService.findPendingGames(currentUser.getMember(), reservationId, page));
    }

    @PutMapping("/status")
    public ApiResponse<String> updateGameStatus(
        @AuthenticationPrincipal PrincipalDetails currentUser,
        @Valid @RequestBody GameStatusUpdateRequest request) {
        return ApiResponse.ok(gameService.updateGameStatus(currentUser.getMember(), request.toServiceRequest()));
    }


}
