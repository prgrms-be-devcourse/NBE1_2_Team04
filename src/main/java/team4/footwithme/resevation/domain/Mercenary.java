package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE mercenary SET is_deleted = 'TRUE' WHERE mercenary_id = ?")
@Entity
public class Mercenary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mercenaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(length = 200, nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Builder
    private Mercenary(Participant participant, String description, Reservation reservation) {
        this.participant = participant;
        this.description = description;
        this.reservation = reservation;
    }

    public static Mercenary create(Participant participant, String description, Reservation reservation) {
        return Mercenary.builder()
            .participant(participant)
            .description(description)
                .reservation(reservation)
            .build();
    }

}
