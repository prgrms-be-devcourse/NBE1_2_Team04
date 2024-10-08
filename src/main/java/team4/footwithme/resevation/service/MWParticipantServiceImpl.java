package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.service.event.ReservationMemberJoinEvent;
import team4.footwithme.chat.service.event.ReservationMembersJoinEvent;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.Mercenary;
import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.repository.MWParticipantRepository;
import team4.footwithme.resevation.repository.MWReservationRepository;
import team4.footwithme.resevation.repository.MercenaryRepository;
import team4.footwithme.resevation.service.request.MWParticipantUpdateServiceResponse;
import team4.footwithme.resevation.service.response.MWParticipantResponse;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.repository.TeamMemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MWParticipantServiceImpl {
    private final MercenaryRepository mercenaryRepository;
    private final MWReservationRepository reservationRepository;
    private final MWParticipantRepository participantRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final ApplicationEventPublisher publisher;

    /**
     * 용병 추가
     * 해당 용병 게시판의 예약에 참가한 인원이면 추가되지 않게 예외처리
     * role은 Pending으로
     */
    @Transactional
    public MWParticipantResponse createMercenaryParticipant(Long mercenaryId, Member member){
        Mercenary mercenary = getMercenaryByMercenaryId(mercenaryId);

        checkParticipantMercenaryByReservationIdAndMemberId(mercenary.getReservation().getReservationId(), member.getMemberId());

        return new MWParticipantResponse(participantRepository.save(Participant.create(mercenary.getReservation(), member, ParticipantRole.PENDING)));
    }

    /**
     * 멤버 추가
     * 해당 예약 팀의 팀원이면 추가 가능
     * 채팅방에도 추가
     */
    @Transactional
    public MWParticipantResponse createParticipant(Long reservationId, Member member){
        Reservation reservation = getReservationByReservationId(reservationId);

        getTeamMember(reservation.getTeam(), member);

        checkParticipantByReservationIdAndMemberId(reservation.getReservationId(), member.getMemberId());

        publisher.publishEvent(new ReservationMemberJoinEvent(member, reservationId));

        return new MWParticipantResponse(participantRepository.save(Participant.create(reservation, member, ParticipantRole.MEMBER)));
    }

    /**
     * 멤버 리스트 한번에 추가
     * 투표가 끝나고 예약이 생성되면 투표 진행한 사람 추가
     * 채팅방에도 추가해주기
     */
    @Transactional
    public void createParticipants(Long reservationId, List<Member> Members){
        Reservation reservation = getReservationByReservationId(reservationId);

        List<Participant> participants = new ArrayList<>();
        for(Member member : Members){
            participants.add(Participant.create(reservation, member, ParticipantRole.MEMBER));
        }

        participantRepository.saveAll(participants);

        publisher.publishEvent(new ReservationMembersJoinEvent(participants, reservationId));
    }

    /**
     * 예약 삭제에 따른 예약 인원 전체 삭제
     * 채팅방에서도 삭제는 예약(채팅방) 삭제 이벤트로 발생함
     */
    @Transactional
    public void deleteParticipants(Long reservationId){
        List<Participant> participants = participantRepository.findParticipantsByReservationId(reservationId);

        participantRepository.deleteAll(participants);
    }

    /**
     * 예약 인원 삭제
     * 예약한 사람만 할 수 있게 (Member는 로그인 한 사람)
     * Accept or Member인 사람은 채팅방에서도 삭제
     * 7명 이상일 때에만 취소 가능하게 구현
     */
    @Transactional
    public Long deleteParticipant(Long reservationId, Member member){
        List<Participant> participants = participantRepository.findParticipantsByReservationId(reservationId);

        if(participants.size()<7){
            throw new IllegalArgumentException(ExceptionMessage.PARTICIPANT_NOT_MEMBER.getText());
        }

        Participant participant = participants.stream().filter(p -> p.getMember().equals(member)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.PARTICIPANT_NOT_IN_MEMBER.getText()));

        participantRepository.delete(participant);

        return member.getMemberId();
    }

    /**
     * 용병의 상태 변경
     * 권한은 해당 참여 인원의 예약자만 가능
     * Accept or ignore
     * Accept시 예약채팅방에도 추가
     */
    @Transactional
    public MWParticipantResponse updateMercenaryParticipant(MWParticipantUpdateServiceResponse response, Member member){
        Participant participant = getParticipantByParticipantId(response.participantId());

        checkReservationCreatedBy(participant.getReservation(), member);

        participant.updateRole(response.role());

        if(response.role() == ParticipantRole.ACCEPT){
            publisher.publishEvent(new ReservationMemberJoinEvent(member, participant.getReservation().getReservationId()));
        }

        return new MWParticipantResponse(participantRepository.save(participant));
    }

    /**
     * 전체 예약 참여자 조회
     * agree, member인 참여자 조회
     */
    @Transactional(readOnly = true)
    public List<MWParticipantResponse> getParticipants(Long reservationId){
        List<Participant> participants = participantRepository.findParticipantsByReservationId(reservationId);

        return participants.stream().map(MWParticipantResponse::new).toList();
    }

    /**
     * 용병 예약 참여자 조회
     * pending인 상태인 참여자 조회
     */
    @Transactional(readOnly = true)
    public List<MWParticipantResponse> getParticipantsMercenary(Long reservationId){
        List<Participant> participants = participantRepository.findParticipantMercenaryByReservationId(reservationId);

        return participants.stream().map(MWParticipantResponse::new).toList();
    }

    private Reservation getReservationByReservationId(Long reservationId) {
        return reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.RESERVATION_NOT_FOUND.getText()));
    }

    private Mercenary getMercenaryByMercenaryId(Long mercenaryId) {
        return mercenaryRepository.findByMercenaryId(mercenaryId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.MERCENARY_NOT_FOUND.getText()));
    }

    private Participant getParticipantByParticipantId(Long participantId) {
        return participantRepository.findParticipantsByParticipantId(participantId)
                .orElseThrow(()->new IllegalArgumentException(ExceptionMessage.PARTICIPANT_NOT_IN_MEMBER.getText()));
    }

    private TeamMember getTeamMember(Team team, Member member) {
        return teamMemberRepository.findByTeamIdAndMemberId(team.getTeamId(), member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_IN_TEAM.getText()));
    }

    private void checkReservationCreatedBy(Reservation reservation, Member member) {
        if(!reservation.getMember().equals(member)) {
            throw new IllegalArgumentException(ExceptionMessage.RESERVATION_NOT_MEMBER.getText());
        }
    }

    private void checkParticipantByReservationIdAndMemberId(Long reservationId, Long memberId) {
        if(participantRepository.findParticipantsByReservationIdAndMemberId(reservationId, memberId).isPresent()){
            throw new IllegalArgumentException(ExceptionMessage.PARTICIPANT_IN_MEMBER.getText());
        }
    }

    private void checkParticipantMercenaryByReservationIdAndMemberId(Long reservationId, Long memberId) {
        if(participantRepository.findParticipantsByReservationIdAndMemberId(reservationId, memberId).isPresent()){
            throw new IllegalArgumentException(ExceptionMessage.MERCENARY_IN_RESERVATION.getText());
        }
    }
}
