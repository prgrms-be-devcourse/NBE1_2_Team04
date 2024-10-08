package team4.footwithme.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.service.event.ReservationMembersJoinEvent;
import team4.footwithme.chat.service.event.ReservationPublishedEvent;
import team4.footwithme.global.exception.CustomException;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.member.domain.Gender;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.reservation.domain.Mercenary;
import team4.footwithme.reservation.domain.Participant;
import team4.footwithme.reservation.domain.ParticipantRole;
import team4.footwithme.reservation.domain.Reservation;
import team4.footwithme.reservation.domain.ReservationStatus;
import team4.footwithme.reservation.repository.MercenaryRepository;
import team4.footwithme.reservation.repository.ParticipantRepository;
import team4.footwithme.reservation.repository.ReservationRepository;
import team4.footwithme.reservation.service.response.ReservationsResponse;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CourtRepository courtRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MercenaryRepository mercenaryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public Slice<ReservationsResponse> findReadyReservations(Long reservationId, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdAt"));
        Reservation reservation = findEntityByIdOrThrowException(reservationRepository, reservationId, ExceptionMessage.RESERVATION_NOT_FOUND);

        if (reservation.getReservationStatus() != ReservationStatus.READY) {
            throw new CustomException(ExceptionMessage.RESERVATION_STATUS_NOT_READY.getText());
        }

        return reservationRepository.findByMatchDateAndCourtAndReservationStatus(
                        reservation.getMatchDate(), reservation.getCourt(), ReservationStatus.READY, pageRequest)
                .map(ReservationsResponse::from);
    }

    @Transactional
    @Override
    public void createReservation(Long memberId, Long courtId, Long teamId, LocalDateTime matchDate, List<Long> memberIds) {
        Court court = courtRepository.findActiveById(courtId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 구장이 없습니다.")
        );
        Member member = memberRepository.findActiveById(memberId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 회원이 없습니다.")
        );
        Team team = teamRepository.findById(teamId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 팀이 없습니다.")
        );

        List<Member> participantMembers = memberRepository.findAllById(memberIds);

        boolean allMale = participantMembers.stream()
            .map(Member::getGender)
            .allMatch(Gender.MALE::equals);

        boolean allFemale = participantMembers.stream()
            .map(Member::getGender)
            .allMatch(Gender.FEMALE::equals);

        Reservation reservation;
        if(memberIds.size() >= 6) {
            // 예약 만들기
            if (allMale) {
                reservation = Reservation.createMaleReadyReservation(court, member, team, matchDate);
            }
            else if (allFemale) {
                reservation = Reservation.createFemaleReadyReservation(court, member, team, matchDate);
            }
            else {
                reservation = Reservation.createMixedReadyReservation(court, member, team, matchDate);
            }
            Reservation savedReservation = reservationRepository.save(reservation);
            List<Participant> participants = participantMembers.stream()
                .map(participantMember -> Participant.create(reservation, participantMember, ParticipantRole.MEMBER))
                .toList();
            participantRepository.saveAll(participants);
            eventPublisher.publishEvent(new ReservationPublishedEvent("예약 채팅방", savedReservation.getReservationId()));
            eventPublisher.publishEvent(new ReservationMembersJoinEvent(participants, savedReservation.getReservationId()));
        }
        else  {
            // 용병 만들기
            if (allMale) {
                reservation = Reservation.createMaleRecruitReservation(court, member, team, matchDate);
            }
            else if (allFemale) {
                reservation = Reservation.createFemaleRecruitReservation(court, member, team, matchDate);
            }
            else {
                reservation = Reservation.createMixedRecruitReservation(court, member, team, matchDate);
            }
            Reservation savedReservation = reservationRepository.save(reservation);
            List<Participant> participants = participantMembers.stream()
                .map(participantMember -> Participant.create(reservation, participantMember, ParticipantRole.MEMBER))
                .toList();
            participantRepository.saveAll(participants);
            Mercenary mercenary = Mercenary.createDefault(reservation);
            mercenaryRepository.save(mercenary);
            eventPublisher.publishEvent(new ReservationPublishedEvent("예약 채팅방", savedReservation.getReservationId()));
            eventPublisher.publishEvent(new ReservationMembersJoinEvent(participants, savedReservation.getReservationId()));
        }
    }

    private <T> T findEntityByIdOrThrowException(CustomGlobalRepository<T> repository, Long id, ExceptionMessage exceptionMessage) {
        return repository.findActiveById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, exceptionMessage);
                    return new IllegalArgumentException(exceptionMessage.getText());
                });
    }
}