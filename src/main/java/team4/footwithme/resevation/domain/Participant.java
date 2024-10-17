package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

@SQLDelete(sql = "UPDATE participant SET is_deleted = 'TRUE' WHERE participant_id = ?")
@Entity
public class Participant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ParticipantRole participantRole;

    private Participant(Reservation reservation, Member member, ParticipantRole participantRole) {
        this.reservation = reservation;
        this.member = member;
        this.participantRole = participantRole;
    }

    protected Participant() {
    }

    public static Participant create(Reservation reservation, Member member, ParticipantRole participantRole) {
        return Participant.builder()
            .reservation(reservation)
            .member(member)
            .participantRole(participantRole)
            .build();
    }

    public static ParticipantBuilder builder() {
        return new ParticipantBuilder();
    }

    public void updateRole(ParticipantRole participantRole) {
        this.participantRole = participantRole;
    }

    public Long getParticipantId() {
        return this.participantId;
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public Member getMember() {
        return this.member;
    }

    public @NotNull ParticipantRole getParticipantRole() {
        return this.participantRole;
    }

    public static class ParticipantBuilder {
        private Reservation reservation;
        private Member member;
        private ParticipantRole participantRole;

        ParticipantBuilder() {
        }

        public ParticipantBuilder reservation(Reservation reservation) {
            this.reservation = reservation;
            return this;
        }

        public ParticipantBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ParticipantBuilder participantRole(ParticipantRole participantRole) {
            this.participantRole = participantRole;
            return this;
        }

        public Participant build() {
            return new Participant(this.reservation, this.member, this.participantRole);
        }

        public String toString() {
            return "Participant.ParticipantBuilder(reservation=" + this.reservation + ", member=" + this.member + ", participantRole=" + this.participantRole + ")";
        }
    }
}
