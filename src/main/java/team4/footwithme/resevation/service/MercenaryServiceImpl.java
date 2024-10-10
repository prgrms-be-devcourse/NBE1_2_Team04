package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.Mercenary;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.repository.MercenaryRepository;
import team4.footwithme.resevation.repository.ReservationRepository;
import team4.footwithme.resevation.service.request.MercenaryServiceRequest;
import team4.footwithme.resevation.service.response.MercenaryResponse;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MercenaryServiceImpl implements MercenaryService {
    private final MercenaryRepository mercenaryRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 용병 게시판 생성
     * 예약자만 생성 가능
     */
    @Transactional
    @Override
    public MercenaryResponse createMercenary(MercenaryServiceRequest request, Member member) {
        Reservation reservation = getReservationByReservationId(request.reservationId());

        checkReservationCreatedBy(reservation, member);

        return new MercenaryResponse(mercenaryRepository.save(Mercenary.create(reservation, makeDescription(request.description(), reservation))));
    }

    /**
     * 단일 용병 게시판 조회
     */
    @Transactional(readOnly = true)
    @Override
    public MercenaryResponse getMercenary(Long mercenaryId) {
        Mercenary mercenary = getMercenaryByMercenaryId(mercenaryId);

        return new MercenaryResponse(mercenary);
    }

    /**
     * 용병 게시판 리스트 조회
     * 해당 용병 게시판의 예약 상태가 RECRUITING 인것만 조회
     * 리스트 및 페이징
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MercenaryResponse> getMercenaries(Pageable pageable) {
        Page<Mercenary> mercenaries = mercenaryRepository.findAllToPage(pageable);
        return mercenaries.map(MercenaryResponse::new);
    }

    /**
     * 용병 게시판 삭제
     * 팀장만 삭제 가능
     */
    @Transactional
    @Override
    public Long deleteMercenary(Long mercenaryId, Member member) {
        Mercenary mercenary = getMercenaryByMercenaryId(mercenaryId);

        checkReservationCreatedBy(mercenary.getReservation(), member);

        mercenaryRepository.delete(mercenary);
        return mercenary.getMercenaryId();
    }


    private String makeDescription(String description, Reservation reservation) {
        return reservation.getMatchDate().format(DateTimeFormatter.ofPattern("'('MM'/'dd HH':'mm')'")) +
            "(" +
            reservation.getCourt().getStadium().getName() +
            ") " +
            description;
    }

    private Reservation getReservationByReservationId(Long reservationId) {
        return reservationRepository.findByReservationId(reservationId)
            .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.RESERVATION_NOT_FOUND.getText()));
    }

    private Mercenary getMercenaryByMercenaryId(Long mercenaryId) {
        return mercenaryRepository.findByMercenaryId(mercenaryId)
            .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.MERCENARY_NOT_FOUND.getText()));
    }

    private void checkReservationCreatedBy(Reservation reservation, Member member) {
        if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException(ExceptionMessage.RESERVATION_NOT_MEMBER.getText());
        }
    }
}
