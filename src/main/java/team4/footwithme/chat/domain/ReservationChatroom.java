package team4.footwithme.chat.domain;

import jakarta.persistence.Entity;

@Entity
public class ReservationChatroom extends Chatroom {

    private Long reservationId;

    private ReservationChatroom(String name, Long reservationId) {
        super(name);
        this.reservationId = reservationId;
    }

    protected ReservationChatroom() {
    }

    public static ReservationChatroom create(String name, Long reservationId) {
        return ReservationChatroom.builder()
            .name(name)
            .reservationId(reservationId)
            .build();
    }

    public static ReservationChatroomBuilder builder() {
        return new ReservationChatroomBuilder();
    }

    public Long getReservationId() {
        return this.reservationId;
    }

    public static class ReservationChatroomBuilder {
        private String name;
        private Long reservationId;

        ReservationChatroomBuilder() {
        }

        public ReservationChatroomBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ReservationChatroomBuilder reservationId(Long reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public ReservationChatroom build() {
            return new ReservationChatroom(this.name, this.reservationId);
        }

        public String toString() {
            return "ReservationChatroom.ReservationChatroomBuilder(name=" + this.name + ", reservationId=" + this.reservationId + ")";
        }
    }
}
