package team4.footwithme.resevation.service;

import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest;
import team4.footwithme.resevation.service.response.GameDetailResponse;

public interface GameService {

    GameDetailResponse registerGame(Member member, GameRegisterServiceRequest request);
}
