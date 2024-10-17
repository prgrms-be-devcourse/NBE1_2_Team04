package team4.footwithme.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

@SQLDelete(sql = "UPDATE team SET is_deleted = 'TRUE' WHERE team_id = ?")
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

    private Team(Long teamId, Long stadiumId, String name, String description, TotalRecord totalRecord, String location) {
        this.teamId = teamId;
        this.stadiumId = stadiumId;
        this.name = name;
        this.description = description;
        this.totalRecord = totalRecord;
        this.location = location;
    }

    protected Team() {
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

    public static TeamBuilder builder() {
        return new TeamBuilder();
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

    public Long getTeamId() {
        return this.teamId;
    }

    public Long getStadiumId() {
        return this.stadiumId;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public TotalRecord getTotalRecord() {
        return this.totalRecord;
    }

    public String getLocation() {
        return this.location;
    }

    public static class TeamBuilder {
        private Long teamId;
        private Long stadiumId;
        private String name;
        private String description;
        private TotalRecord totalRecord;
        private String location;

        TeamBuilder() {
        }

        public TeamBuilder teamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public TeamBuilder stadiumId(Long stadiumId) {
            this.stadiumId = stadiumId;
            return this;
        }

        public TeamBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TeamBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TeamBuilder totalRecord(TotalRecord totalRecord) {
            this.totalRecord = totalRecord;
            return this;
        }

        public TeamBuilder location(String location) {
            this.location = location;
            return this;
        }

        public Team build() {
            return new Team(this.teamId, this.stadiumId, this.name, this.description, this.totalRecord, this.location);
        }

        public String toString() {
            return "Team.TeamBuilder(teamId=" + this.teamId + ", stadiumId=" + this.stadiumId + ", name=" + this.name + ", description=" + this.description + ", totalRecord=" + this.totalRecord + ", location=" + this.location + ")";
        }
    }
}
