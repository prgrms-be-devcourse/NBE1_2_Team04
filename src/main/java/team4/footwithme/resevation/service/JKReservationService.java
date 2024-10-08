package team4.footwithme.resevation.service;

import team4.footwithme.member.domain.Member;

public interface JKReservationService {
    Long deleteReservation(Long reservationId, Member member);
}
