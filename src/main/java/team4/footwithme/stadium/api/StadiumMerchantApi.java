package team4.footwithme.stadium.api;

import org.slf4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.stadium.api.request.StadiumRegisterRequest;
import team4.footwithme.stadium.api.request.StadiumUpdateRequest;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;

@RestController
@RequestMapping("/api/v1/merchant/stadium")
public class StadiumMerchantApi {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(StadiumMerchantApi.class);
    private final StadiumService stadiumService;

    public StadiumMerchantApi(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/register")
    public ApiResponse<StadiumDetailResponse> registerStadium(
        @Validated @RequestBody StadiumRegisterRequest request,
        @AuthenticationPrincipal PrincipalDetails currentUser) {
        return ApiResponse.created(stadiumService.registerStadium(request.toServiceRequest(), currentUser.getMember()));
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PutMapping("/{stadiumId}")
    public ApiResponse<StadiumDetailResponse> updateStadium(
        @PathVariable Long stadiumId,
        @Validated @RequestBody StadiumUpdateRequest request,
        @AuthenticationPrincipal PrincipalDetails currentUser) {
        return ApiResponse.ok(stadiumService.updateStadium(request.toServiceRequest(), currentUser.getMember(), stadiumId));
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @DeleteMapping("/{stadiumId}")
    public ApiResponse<Void> deleteStadium(
        @PathVariable Long stadiumId,
        @AuthenticationPrincipal PrincipalDetails currentUser) {
        stadiumService.deleteStadium(currentUser.getMember(), stadiumId);
        return ApiResponse.ok(null);
    }
}