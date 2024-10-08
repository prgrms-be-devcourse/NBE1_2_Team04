package team4.footwithme.resevation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.api.request.MWMercenaryRequest;
import team4.footwithme.resevation.service.MercenaryServiceImpl;
import team4.footwithme.resevation.service.response.MWMercenaryResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mercenary")
public class MWMercenaryApi {
    private final MercenaryServiceImpl mercenaryService;

    @PostMapping
    public ApiResponse<MWMercenaryResponse> createMercenary(@RequestBody @Valid MWMercenaryRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApiResponse.created(mercenaryService.createMercenary(request.toServiceRequest(), principalDetails.getMember()));
    }

    @GetMapping("/{mercenaryId}")
    public ApiResponse<MWMercenaryResponse> getMercenary(@PathVariable Long mercenaryId){
        return ApiResponse.ok(mercenaryService.getMercenary(mercenaryId));
    }

    @GetMapping
    public ApiResponse<Page<MWMercenaryResponse>> getMercenaries(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return ApiResponse.ok(mercenaryService.getMercenaries(pageRequest));
    }

    @DeleteMapping("/{mercenaryId}")
    public ApiResponse<Long> deleteMercenary(@PathVariable Long mercenaryId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApiResponse.ok(mercenaryService.deleteMercenary(mercenaryId, principalDetails.getMember()));
    }
}
