package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.service.StadiumService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class StadiumApi {

    private final StadiumService stadiumService;

    @GetMapping("/stadiums")
    public ApiResponse<List<Stadium>> stadiums() {
        List<Stadium> stadiumList = stadiumService.getStadiums();
        return ApiResponse.ok(stadiumList);
    }


}
