package team4.footwithme.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

@Getter
@Entity
@SQLDelete(sql = "UPDATE team_rate SET is_deleted = TRUE WHERE team_rate_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamRateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    private double rating;

    @Column(length = 100, nullable = true)
    private String evaluation;

    //테스트 용 생성자
    public TeamRate(Team team, double rating, String evaluation) {
        this.team = team;
        this.rating = rating;
        this.evaluation = evaluation;
    }

}
