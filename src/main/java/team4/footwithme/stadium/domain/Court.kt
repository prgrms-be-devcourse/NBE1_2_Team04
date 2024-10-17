package team4.footwithme.stadium.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.global.exception.ExceptionMessage;

import java.math.BigDecimal;

@SQLDelete(sql = "UPDATE court SET is_deleted = 'TRUE' WHERE court_id = ?")
@Entity
public class Court extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courtId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @NotNull
    private String name;

    @Column(length = 200, nullable = true)
    private String description;

    @NotNull
    private BigDecimal pricePerHour;

    private Court(Stadium stadium, String name, String description, BigDecimal pricePerHour) {
        this.stadium = stadium;
        this.name = name;
        this.description = description;
        this.pricePerHour = pricePerHour;
    }

    protected Court() {
    }

    public static Court create(Stadium stadium, String name, String description, BigDecimal pricePerHour) {
        return Court.builder()
            .stadium(stadium)
            .name(name)
            .description(description)
            .pricePerHour(pricePerHour)
            .build();
    }

    public static CourtBuilder builder() {
        return new CourtBuilder();
    }

    public void updateCourt(Long stadiumId, Long memberId, String name, String description, BigDecimal pricePerHour) {
        checkMember(memberId);
        checkStadium(stadiumId);
        this.name = name;
        this.description = description;
        this.pricePerHour = pricePerHour;
    }

    public void deleteCourt(Long stadiumId, Long memberId) {
        checkMember(memberId);
        checkStadium(stadiumId);
    }

    private void checkStadium(Long stadiumId) {
        if (!this.stadium.getStadiumId().equals(stadiumId)) {
            throw new IllegalArgumentException(ExceptionMessage.COURT_NOT_OWNED_BY_STADIUM.getText());
        }
    }

    private void checkMember(Long memberId) {
        if (!this.stadium.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }
    }

    public Long getCourtId() {
        return this.courtId;
    }

    public Stadium getStadium() {
        return this.stadium;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public @NotNull BigDecimal getPricePerHour() {
        return this.pricePerHour;
    }

    public static class CourtBuilder {
        private Stadium stadium;
        private String name;
        private String description;
        private BigDecimal pricePerHour;

        CourtBuilder() {
        }

        public CourtBuilder stadium(Stadium stadium) {
            this.stadium = stadium;
            return this;
        }

        public CourtBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CourtBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CourtBuilder pricePerHour(BigDecimal pricePerHour) {
            this.pricePerHour = pricePerHour;
            return this;
        }

        public Court build() {
            return new Court(this.stadium, this.name, this.description, this.pricePerHour);
        }

        public String toString() {
            return "Court.CourtBuilder(stadium=" + this.stadium + ", name=" + this.name + ", description=" + this.description + ", pricePerHour=" + this.pricePerHour + ")";
        }
    }
}
