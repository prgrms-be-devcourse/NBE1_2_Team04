package team4.footwithme.stadium.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.global.exception.ExceptionMessage;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private Court(Stadium stadium, String name, String description, BigDecimal pricePerHour) {
        this.stadium = stadium;
        this.name = name;
        this.description = description;
        this.pricePerHour = pricePerHour;
    }

    public static Court create(Stadium stadium, String name, String description, BigDecimal pricePerHour) {
        return Court.builder()
                .stadium(stadium)
                .name(name)
                .description(description)
                .pricePerHour(pricePerHour)
                .build();
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
}
