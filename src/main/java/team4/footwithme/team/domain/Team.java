package team4.footwithme.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.stadium.domain.Stadium;
import org.hibernate.annotations.SQLDelete;

@Getter
@Builder
@SQLDelete(sql = "UPDATE team SET is_deleted = TRUE WHERE team_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @NotNull
    private Long chatRoomId;

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
    private Team(Long teamId, Stadium stadium, Long chatRoomId, String name, String description, TotalRecord totalRecord, String location) {
        this.teamId = teamId;
        this.stadium = stadium;
        this.chatRoomId = chatRoomId;
        this.name = name;
        this.description = description;
        this.totalRecord = totalRecord;
        this.location = location;
    }

    public static Team create(Stadium stadium, Long chatRoomId, String name, String description, int winCount, int drawCount, int loseCount, String location) {
        return Team.builder()
            .stadium(stadium)
            .chatRoomId(chatRoomId)
            .name(name)
            .description(description)
            .totalRecord(TotalRecord.builder()
                .winCount(winCount)
                .drawCount(drawCount)
                .loseCount(loseCount)
                .build())
            .location(location)
            .build();
    }

}
