package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.service.event.ReservationDeletedEvent;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.*;
import team4.footwithme.resevation.repository.JKGameRepository;
import team4.footwithme.resevation.repository.JKMercenaryRepository;
import team4.footwithme.resevation.repository.JKParticipantRepository;
import team4.footwithme.resevation.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JKReservationServiceImpl implements JKReservationService {

    private final ReservationRepository reservationRepository;
    private final JKParticipantRepository participantRepository;
    private final JKGameRepository gameRepository;
    private final JKMercenaryRepository mercenaryRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public Long deleteReservation(Long reservationId, Member member) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if(reservation.getReservationStatus() != ReservationStatus.RECRUITING){
            throw new IllegalArgumentException("취소할 수 없는 예약 입니다.");
        }

        if(!reservation.getMember().getMemberId().equals(member.getMemberId())){
            throw new IllegalArgumentException("예약한 사람만이 취소할 수 있습니다.");
        }

        deleteGames(reservationId);
        deleteMercenaries(reservationId);
        deleteParticipants(reservationId);

        reservationRepository.delete(reservation);
        publisher.publishEvent(new ReservationDeletedEvent(reservationId));

        return reservationId;
    }

    @Transactional
    public void deleteGames(Long reservationId){
        List<Game> games = gameRepository.findAllByReservationId(reservationId);
        gameRepository.deleteAllInBatch(games);
    }

    @Transactional
    public void deleteMercenaries(Long reservationId){
        List<Mercenary> mercenaries = mercenaryRepository.findAllMercenaryByReservationId(reservationId);
        mercenaryRepository.deleteAllInBatch(mercenaries);
    }

    @Transactional
    public void deleteParticipants(Long reservationId){
        List<Participant> participants = participantRepository.findAllByReservationId(reservationId);
        participantRepository.deleteAllInBatch(participants);
    }


}
