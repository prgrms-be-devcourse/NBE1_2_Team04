package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE participant SET is_deleted = TRUE WHERE participant_id = ?")
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

    @Builder
    private Participant(Reservation reservation, Member member, ParticipantRole participantRole) {
        this.reservation = reservation;
        this.member = member;
        this.participantRole = participantRole;
    }

    public static Participant create(Reservation reservation, Member member, ParticipantRole participantRole) {
        return Participant.builder()
            .reservation(reservation)
            .member(member)
            .participantRole(participantRole)
            .build();
    }

    public void updateRole(ParticipantRole participantRole) {
        this.participantRole = participantRole;
    }

}
