package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.stadium.service.CourtService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/court")
public class CourtApi {

    private final CourtService courtService;



}
