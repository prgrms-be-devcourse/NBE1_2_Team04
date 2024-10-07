package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.CustomException;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.Game;
import team4.footwithme.resevation.domain.GameStatus;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.resevation.repository.GameRepository;
import team4.footwithme.resevation.repository.ReservationRepository;
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest;
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest;
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
            throw new IllegalArgumentException(ExceptionMessage.RESERVATION_MEMBER_NOT_MATCH.getText());
        }

        Game game = Game.create(reservation1, reservation2, GameStatus.PENDING);

        gameRepository.save(game);

        return GameDetailResponse.from(game);
    }


    @Transactional
    public String updateGameStatus(Member member, GameStatusUpdateServiceRequest request) {
        Game game = (Game) findEntityByIdOrThrowException(gameRepository, request.gameId(), ExceptionMessage.GAME_NOT_FOUND);

        game.update(request.status());

        Reservation firstReservation = game.getFirstTeamReservation();
        Reservation secondReservation = game.getSecondTeamReservation();

        if (request.status() == GameStatus.READY) {
            boolean isConflict = reservationRepository.findByMatchDateAndCourtAndReservationStatus(
                            firstReservation.getMatchDate(), firstReservation.getCourt(), ReservationStatus.CONFIRMED, PageRequest.of(0, 1))
                    .hasContent();

            if (isConflict) {
                game.update(GameStatus.IGNORE);
                firstReservation.updateStatus(ReservationStatus.CANCELLED);
                secondReservation.updateStatus(ReservationStatus.CANCELLED);
                return ExceptionMessage.RESERVATION_CONFLICT.getText();
            }

            firstReservation.updateStatus(ReservationStatus.CONFIRMED);
            secondReservation.updateStatus(ReservationStatus.CONFIRMED);
            return ExceptionMessage.RESERVATION_SUCCESS.getText();
        }

        //TODO 매칭 실패시 삭제해야함
        return "해당 매칭은 거절하였습니다.";
    }

    public Slice<GameDetailResponse> findPendingGames(Member member, Long reservationId, Integer page) {
        Reservation reservation = (Reservation) findEntityByIdOrThrowException(reservationRepository, reservationId, ExceptionMessage.RESERVATION_NOT_FOUND);

        if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException(ExceptionMessage.RESERVATION_MEMBER_NOT_MATCH.getText());
        }

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdAt"));
        return gameRepository.findBySecondReservationAndStatus(
                        reservation, GameStatus.PENDING, pageRequest)
                .map(GameDetailResponse::from);
    }


    private <T> T findEntityByIdOrThrowException(CustomGlobalRepository<T> repository, Long id, ExceptionMessage exceptionMessage) {
        return repository.findActiveById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, exceptionMessage);
                    return new IllegalArgumentException(exceptionMessage.getText());
                });
    }
}
