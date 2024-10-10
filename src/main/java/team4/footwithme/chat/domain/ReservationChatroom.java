package team4.footwithme.chat.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationChatroom extends Chatroom {

    private Long reservationId;

    @Builder
    private ReservationChatroom(String name, Long reservationId) {
        super(name);
        this.reservationId = reservationId;
    }

    public static ReservationChatroom create(String name, Long reservationId) {
        return ReservationChatroom.builder()
            .name(name)
            .reservationId(reservationId)
            .build();
    }
}
