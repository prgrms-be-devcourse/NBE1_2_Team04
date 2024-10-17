package team4.footwithme.resevation.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.chat.service.event.ReservationDeletedEvent
import team4.footwithme.chat.service.event.ReservationMembersJoinEvent
import team4.footwithme.chat.service.event.ReservationPublishedEvent
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.member.domain.Gender
import team4.footwithme.member.domain.Member
import team4.footwithme.member.repository.MemberRepository
import team4.footwithme.resevation.domain.*
import team4.footwithme.resevation.repository.GameRepository
import team4.footwithme.resevation.repository.MercenaryRepository
import team4.footwithme.resevation.repository.ParticipantRepository
import team4.footwithme.resevation.repository.ReservationRepository
import team4.footwithme.resevation.service.response.ReservationInfoDetailsResponse
import team4.footwithme.resevation.service.response.ReservationInfoResponse
import team4.footwithme.resevation.service.response.ReservationsResponse
import team4.footwithme.stadium.repository.CourtRepository
import team4.footwithme.team.repository.TeamRepository
import java.time.LocalDateTime
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val courtRepository: CourtRepository,
    private val memberRepository: MemberRepository,
    private val teamRepository: TeamRepository,
    private val participantRepository: ParticipantRepository,
    private val mercenaryRepository: MercenaryRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val gameRepository: GameRepository
) : ReservationService {
    @Transactional(readOnly = true)
    override fun findReadyReservations(reservationId: Long?, page: Int?): Slice<ReservationsResponse> {
        val pageRequest = PageRequest.of(page!!, 10, Sort.by(Sort.Direction.ASC, "createdAt"))
        val reservation = findEntityByIdOrThrowException(
            reservationRepository,
            reservationId,
            ExceptionMessage.RESERVATION_NOT_FOUND
        ) as Reservation

        require(reservation.reservationStatus == ReservationStatus.READY) { ExceptionMessage.RESERVATION_STATUS_NOT_READY.text }

        return reservationRepository.findByMatchDateAndCourtAndReservationStatus(
            reservationId, reservation.matchDate, reservation.court, ReservationStatus.READY, pageRequest
        )
            .map<ReservationsResponse>(Function<Reservation?, ReservationsResponse> { reservation: Reservation? ->
                ReservationsResponse.Companion.from(
                    reservation
                )
            })
    }

    @Transactional
    override fun createReservation(
        memberId: Long?,
        courtId: Long?,
        teamId: Long,
        matchDate: LocalDateTime?,
        memberIds: List<Long?>
    ) {
        val court = courtRepository.findActiveById(courtId).orElseThrow { IllegalArgumentException("해당하는 구장이 없습니다.") }
        val member: Member = memberRepository.findActiveById(memberId).orElseThrow(
            Supplier { IllegalArgumentException("해당하는 회원이 없습니다.") }
        ) as Member
        val team = teamRepository.findById(teamId).orElseThrow { IllegalArgumentException("해당하는 팀이 없습니다.") }!!

        val participantMembers = memberRepository.findAllById(memberIds)

        val allMale = participantMembers.stream()
            .map<Gender>(Member::gender)
            .allMatch { other: Gender? -> Gender.MALE.equals(other) }

        val allFemale = participantMembers.stream()
            .map<Gender>(Member::gender)
            .allMatch { other: Gender? -> Gender.FEMALE.equals(other) }

        val reservation: Reservation
        if (memberIds.size >= 6) {
            // 예약 만들기
            reservation = if (allMale) {
                Reservation.Companion.createMaleReadyReservation(court, member, team, matchDate)
            } else if (allFemale) {
                Reservation.Companion.createFemaleReadyReservation(court, member, team, matchDate)
            } else {
                Reservation.Companion.createMixedReadyReservation(court, member, team, matchDate)
            }
            val savedReservation = reservationRepository.save(reservation)
            val participants = participantMembers.stream()
                .map<Participant>(Function<Member?, Participant> { participantMember: Member? ->
                    Participant.Companion.create(
                        reservation,
                        participantMember,
                        ParticipantRole.MEMBER
                    )
                })
                .toList()
            participantRepository.saveAll(participants)
            eventPublisher.publishEvent(ReservationPublishedEvent("예약 채팅방", savedReservation.reservationId))
            eventPublisher.publishEvent(ReservationMembersJoinEvent(participants, savedReservation.reservationId))
        } else {
            // 용병 만들기
            reservation = if (allMale) {
                Reservation.Companion.createMaleRecruitReservation(court, member, team, matchDate)
            } else if (allFemale) {
                Reservation.Companion.createFemaleRecruitReservation(court, member, team, matchDate)
            } else {
                Reservation.Companion.createMixedRecruitReservation(court, member, team, matchDate)
            }
            val savedReservation = reservationRepository.save(reservation)
            val participants = participantMembers.stream()
                .map<Participant>(Function<Member?, Participant> { participantMember: Member? ->
                    Participant.Companion.create(
                        reservation,
                        participantMember,
                        ParticipantRole.MEMBER
                    )
                })
                .toList()
            participantRepository.saveAll(participants)
            val mercenary: Mercenary = Mercenary.Companion.createDefault(reservation)
            mercenaryRepository.save(mercenary)
            eventPublisher.publishEvent(ReservationPublishedEvent("예약 채팅방", savedReservation.reservationId))
            eventPublisher.publishEvent(ReservationMembersJoinEvent(participants, savedReservation.reservationId))
        }
    }

    @Transactional(readOnly = true)
    override fun getTeamReservationInfo(teamId: Long?): List<ReservationInfoResponse> {
        val reservations = findByTeamTeamIdOrThrowException(teamId)

        val list = reservations!!.stream()
            .map<ReservationInfoResponse>(Function<Reservation?, ReservationInfoResponse> { reservation: Reservation? ->
                ReservationInfoResponse.Companion.from(
                    reservation
                )
            })
            .collect(Collectors.toList<ReservationInfoResponse>())

        return list
    }

    @Transactional(readOnly = true)
    override fun getTeamReservationInfoDetails(reservationId: Long): ReservationInfoDetailsResponse {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { IllegalArgumentException("해당 예약을 찾을 수 없습니다.") }!!

        val participants = participantRepository.findParticipantsByReservationId(reservationId)

        //상대팀 조회
        val matchedTeam = gameRepository.findFirstTeamReservationBySecondTeamReservationId(reservationId)
            .orElse(null)
        //상대팀 이름 --> 없으면 null
        val matchTeamName = matchedTeam?.team?.name

        return ReservationInfoDetailsResponse.Companion.of(reservation, participants, matchTeamName)
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

    fun findByTeamTeamIdOrThrowException(teamId: Long?): List<Reservation?>? {
        val result = reservationRepository.findByTeamTeamId(teamId)
        require(!result!!.isEmpty()) { "해당 팀이 존재하지 않습니다." }
        return result
    }

    @Transactional
    override fun deleteReservation(reservationId: Long, member: Member?): Long {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 예약입니다.") }!!

        require(reservation.reservationStatus == ReservationStatus.RECRUITING) { "취소할 수 없는 예약 입니다." }

        require(reservation.member!!.memberId == member!!.memberId) { "예약한 사람만이 취소할 수 있습니다." }

        deleteGames(reservationId)
        deleteMercenaries(reservationId)
        deleteParticipants(reservationId)

        reservationRepository.delete(reservation)
        eventPublisher.publishEvent(ReservationDeletedEvent(reservationId))

        return reservationId
    }

    @Transactional
    fun deleteGames(reservationId: Long?) {
        val games = gameRepository.findAllByReservationId(reservationId)
        gameRepository.deleteAllInBatch(games)
    }

    @Transactional
    fun deleteMercenaries(reservationId: Long?) {
        val mercenaries = mercenaryRepository.findAllMercenaryByReservationId(reservationId)
        mercenaryRepository.deleteAllInBatch(mercenaries)
    }

    @Transactional
    fun deleteParticipants(reservationId: Long?) {
        val participants = participantRepository.findAllByReservationId(reservationId)
        participantRepository.deleteAllInBatch(participants)
    }


    companion object {
        private val log: Logger = LoggerFactory.getLogger(ReservationServiceImpl::class.java)
    }
}