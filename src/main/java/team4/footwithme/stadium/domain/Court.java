package team4.footwithme.stadium.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

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

    // TODO : 검증에 대한 책임 한번 생각해보자
    public void updateCourt(String name, String description, BigDecimal pricePerHour) {
        this.name = name;
        this.description = description;
        this.pricePerHour = pricePerHour;
    }

}
