package team4.footwithme.resevation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.api.request.MercenaryRequest;
import team4.footwithme.resevation.service.MercenaryService;
import team4.footwithme.resevation.service.response.MercenaryResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mercenary")
public class MercenaryApi {
    private final MercenaryService mercenaryService;

    @PostMapping
    public ApiResponse<MercenaryResponse> createMercenary(@RequestBody @Valid MercenaryRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApiResponse.created(mercenaryService.createMercenary(request.toServiceRequest(), principalDetails.getMember()));
    }

    @GetMapping("/{mercenaryId}")
    public ApiResponse<MercenaryResponse> getMercenary(@PathVariable Long mercenaryId){
        return ApiResponse.ok(mercenaryService.getMercenary(mercenaryId));
    }

    @GetMapping
    public ApiResponse<Page<MercenaryResponse>> getMercenaries(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return ApiResponse.ok(mercenaryService.getMercenaries(pageRequest));
    }

    @DeleteMapping("/{mercenaryId}")
    public ApiResponse<Long> deleteMercenary(@PathVariable Long mercenaryId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApiResponse.ok(mercenaryService.deleteMercenary(mercenaryId, principalDetails.getMember()));
    }
}
