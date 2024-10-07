package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.M;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.CustomException;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.Game;
import team4.footwithme.resevation.domain.GameStatus;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.repository.GameRepository;
import team4.footwithme.resevation.repository.ReservationRepository;
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest;
import team4.footwithme.resevation.service.response.GameDetailResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final ReservationRepository reservationRepository;
    private final GameRepository gameRepository;

    @Transactional
    public GameDetailResponse registerGame(Member member, GameRegisterServiceRequest request) {
        Reservation reservation1 = (Reservation) findEntityByIdOrThrowException(reservationRepository, request.firstReservationId(), ExceptionMessage.RESERVATION_NOT_FOUND);
        Reservation reservation2 = (Reservation) findEntityByIdOrThrowException(reservationRepository, request.secondReservationId(), ExceptionMessage.RESERVATION_NOT_FOUND);

        if (!reservation1.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ExceptionMessage.RESERVATION_MEMBER_NOT_MATCH.getText());
        }

        Game game = Game.create(reservation1, reservation2, GameStatus.PENDING);

        gameRepository.save(game);

        return GameDetailResponse.from(game);
    }


    private <T> T findEntityByIdOrThrowException(CustomGlobalRepository<T> repository, Long id, ExceptionMessage exceptionMessage) {
        return repository.findActiveById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, exceptionMessage);
                    return new IllegalArgumentException(exceptionMessage.getText());
                });
    }
}
