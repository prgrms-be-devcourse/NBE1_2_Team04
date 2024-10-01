package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.stadium.api.request.CourtRegisterRequest;
import team4.footwithme.stadium.api.request.CourtUpdateRequest;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.response.CourtDetailResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/merchant/court")
public class CourtMerchantApi {

    private final CourtService courtService;

//    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/register")
    public ApiResponse<CourtDetailResponse> registerCourt(@Validated @RequestBody CourtRegisterRequest request,
                                                          @AuthenticationPrincipal PrincipalDetails currentUser) {

        return ApiResponse.created();
    }

//    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PutMapping("/{courtId}")
    public ApiResponse<CourtDetailResponse> updateCourt(@PathVariable Long courtId, @Validated @RequestBody CourtRegisterRequest request,
                                                      @AuthenticationPrincipal PrincipalDetails currentUser) {

        return ApiResponse.ok();
    }

//    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @DeleteMapping("/{courtId}")
    public ApiResponse<Void> deleteCourt(@PathVariable Long courtId,
                                         @AuthenticationPrincipal PrincipalDetails currentUser) {
        return ApiResponse.ok(null);
    }
}
