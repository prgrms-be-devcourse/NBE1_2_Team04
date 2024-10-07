package team4.footwithme.resevation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.api.request.GameRegisterRequest;
import team4.footwithme.resevation.domain.Game;
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
}
