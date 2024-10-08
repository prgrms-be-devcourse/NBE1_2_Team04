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
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(length = 200, nullable = true)
    private String description;

    @Builder
    private Mercenary(Reservation reservation, String description) {
        this.reservation = reservation;
        this.description = description;
    }

    public static Mercenary create(Reservation reservation, String description) {
        return Mercenary.builder()
            .reservation(reservation)
            .description(description)
            .build();
    }

    public static Mercenary createDefault(Reservation reservation) {
        return Mercenary.builder()
            .reservation(reservation)
            .description("기본 설명")
            .build();
    }
}
