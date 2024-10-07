package team4.footwithme.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

@Getter
@SQLDelete(sql = "UPDATE team SET is_deleted = 'TRUE' WHERE team_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = true)
    private Long stadiumId;

    @NotNull
    @Column(length = 50)
    private String name;

    @Column(length = 200, nullable = true)
    private String description;

    @Embedded
    private TotalRecord totalRecord;

    @Column(length = 100, nullable = true)
    private String location;

    @Builder
    private Team(Long teamId, Long stadiumId, String name, String description, TotalRecord totalRecord, String location) {
        this.teamId = teamId;
        this.stadiumId = stadiumId;
        this.name = name;
        this.description = description;
        this.totalRecord = totalRecord;
        this.location = location;
    }

    public static Team create(Long stadiumId, String name, String description, int winCount, int drawCount, int loseCount, String location) {
        return Team.builder()
            .stadiumId(stadiumId)
            .name(name)
            .description(description)
            .totalRecord(TotalRecord.builder().build())
            .location(location)
            .build();
    }

    public void updateName(@NotNull String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateLocation(String location) {
        this.location = location;
    }
}
