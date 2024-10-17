package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

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

    private Mercenary(Reservation reservation, String description) {
        this.description = description;
        this.reservation = reservation;
    }

    protected Mercenary() {
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

    public static MercenaryBuilder builder() {
        return new MercenaryBuilder();
    }

    public Long getMercenaryId() {
        return this.mercenaryId;
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public String getDescription() {
        return this.description;
    }

    public static class MercenaryBuilder {
        private Reservation reservation;
        private String description;

        MercenaryBuilder() {
        }

        public MercenaryBuilder reservation(Reservation reservation) {
            this.reservation = reservation;
            return this;
        }

        public MercenaryBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Mercenary build() {
            return new Mercenary(this.reservation, this.description);
        }

        public String toString() {
            return "Mercenary.MercenaryBuilder(reservation=" + this.reservation + ", description=" + this.description + ")";
        }
    }
}
