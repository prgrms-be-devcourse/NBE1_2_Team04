package team4.footwithme.resevation.service;

import org.springframework.data.domain.Slice;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest;
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest;
import team4.footwithme.resevation.service.response.GameDetailResponse;

public interface GameService {

    GameDetailResponse registerGame(Member member, GameRegisterServiceRequest request);

    String updateGameStatus(Member member, GameStatusUpdateServiceRequest request);

    Slice<GameDetailResponse> findPendingGames(Member member, Long reservationId, Integer page);
}
