package team4.footwithme.resevation.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.domain.Game
import team4.footwithme.resevation.domain.GameStatus
import team4.footwithme.resevation.domain.Reservation
import team4.footwithme.resevation.domain.ReservationStatus
import team4.footwithme.resevation.repository.GameRepository
import team4.footwithme.resevation.repository.ReservationRepository
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest
import team4.footwithme.resevation.service.response.GameDetailResponse
import java.util.function.Function

@Service
class GameServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val gameRepository: GameRepository
) : GameService {
    @Transactional
    override fun registerGame(member: Member?, request: GameRegisterServiceRequest?): GameDetailResponse {
        val reservation1 = findEntityByIdOrThrowException(
            reservationRepository,
            request!!.firstReservationId,
            ExceptionMessage.RESERVATION_NOT_FOUND
        ) as Reservation
        val reservation2 = findEntityByIdOrThrowException(
            reservationRepository,
            request.secondReservationId,
            ExceptionMessage.RESERVATION_NOT_FOUND
        ) as Reservation

        require(reservation1.member!!.memberId == member!!.memberId) { ExceptionMessage.RESERVATION_MEMBER_NOT_MATCH.text }

        val game: Game = Game.Companion.create(reservation1, reservation2, GameStatus.PENDING)

        gameRepository.save(game)

        return GameDetailResponse.Companion.from(game)
    }


    @Transactional
    override fun updateGameStatus(member: Member?, request: GameStatusUpdateServiceRequest?): String? {
        require((request!!.status == GameStatus.READY || request.status == GameStatus.IGNORE)) { ExceptionMessage.GAME_STATUS_NOT_VALID.text }

        val game =
            findEntityByIdOrThrowException(gameRepository, request.gameId, ExceptionMessage.GAME_NOT_FOUND) as Game

        game.update(request.status)

        if (request.status == GameStatus.READY) {
            val firstReservation = findEntityByIdOrThrowException(
                reservationRepository,
                game.firstTeamReservation!!.reservationId,
                ExceptionMessage.RESERVATION_NOT_FOUND
            ) as Reservation
            val secondReservation = findEntityByIdOrThrowException(
                reservationRepository,
                game.secondTeamReservation!!.reservationId,
                ExceptionMessage.RESERVATION_NOT_FOUND
            ) as Reservation

            require(!(firstReservation.reservationStatus != ReservationStatus.READY || secondReservation.reservationStatus != ReservationStatus.READY)) { ExceptionMessage.RESERVATION_STATUS_NOT_READY.text }

            val isConflict = reservationRepository.findByMatchDateAndCourtAndReservationStatus(
                -1L,
                firstReservation.matchDate,
                firstReservation.court,
                ReservationStatus.CONFIRMED,
                PageRequest.of(0, 1)
            )
                .hasContent()

            if (isConflict) {
                game.update(GameStatus.IGNORE)
                firstReservation.updateStatus(ReservationStatus.CANCELLED)
                secondReservation.updateStatus(ReservationStatus.CANCELLED)
                gameRepository.softDeleteBySecondTeamReservation(secondReservation)
                return ExceptionMessage.RESERVATION_CONFLICT.text
            }

            firstReservation.updateStatus(ReservationStatus.CONFIRMED)
            secondReservation.updateStatus(ReservationStatus.CONFIRMED)
            gameRepository.softDeleteBySecondTeamReservation(secondReservation)
            return ExceptionMessage.RESERVATION_SUCCESS.text
        }

        gameRepository.delete(game)
        return "해당 매칭을 거절하였습니다."
    }

    override fun findPendingGames(member: Member?, reservationId: Long?, page: Int?): Slice<GameDetailResponse> {
        val reservation = findEntityByIdOrThrowException(
            reservationRepository,
            reservationId,
            ExceptionMessage.RESERVATION_NOT_FOUND
        ) as Reservation

        require(reservation.member!!.memberId == member!!.memberId) { ExceptionMessage.RESERVATION_MEMBER_NOT_MATCH.text }

        val pageRequest = PageRequest.of(page!!, 10, Sort.by(Sort.Direction.ASC, "createdAt"))
        return gameRepository.findBySecondReservationAndStatus(
            reservation, GameStatus.PENDING, pageRequest
        )
            .map<GameDetailResponse>(Function<Game?, GameDetailResponse> { game: Game? ->
                GameDetailResponse.Companion.from(
                    game
                )
            })
    }


    private fun <T> findEntityByIdOrThrowException(
        repository: CustomGlobalRepository<T?>,
        id: Long?,
        exceptionMessage: ExceptionMessage
    ): T? {
        return repository.findActiveById(id)
            .orElseThrow {
                log.warn(">>>> {} : {} <<<<", id, exceptionMessage)
                IllegalArgumentException(exceptionMessage.text)
            }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GameServiceImpl::class.java)
    }
}
