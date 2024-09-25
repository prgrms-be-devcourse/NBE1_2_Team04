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
}
