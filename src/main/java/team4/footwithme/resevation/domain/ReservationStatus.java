package team4.footwithme.resevation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    CONFIRMED("상대팀과 매치 확정"),
    CANCELLED("예약 취소"),
    RECRUITING("인원이 6명 아니면 RECRUITING"),
    READY("인원이 6명 시작 되면 READY");

    private final String description;
}
