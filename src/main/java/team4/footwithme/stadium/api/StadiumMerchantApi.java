package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.stadium.api.request.StadiumRegisterRequest;
import team4.footwithme.stadium.api.request.StadiumUpdateRequest;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/merchant/stadium")
public class StadiumMerchantApi {

    private final StadiumService stadiumService;

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/register")
    public ApiResponse<StadiumDetailResponse> registerStadium(
            @Validated @RequestBody StadiumRegisterRequest request,
            @AuthenticationPrincipal PrincipalDetails currentUser) {
        return ApiResponse.created(stadiumService.registerStadium(request.toServiceRequest(), currentUser.getMember().getMemberId()));
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PutMapping("/{stadiumId}")
    public ApiResponse<StadiumDetailResponse> updateStadium(
            @PathVariable Long stadiumId,
            @Validated @RequestBody StadiumUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails currentUser) {
        return ApiResponse.ok(stadiumService.updateStadium(request.toServiceRequest(), currentUser.getMember().getMemberId(), stadiumId));
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @DeleteMapping("/{stadiumId}")
    public ApiResponse<Void> deleteStadium(
            @PathVariable Long stadiumId,
            @AuthenticationPrincipal PrincipalDetails currentUser) {
        stadiumService.deleteStadium(currentUser.getMember().getMemberId(), stadiumId);
        return ApiResponse.ok(null);
    }
}