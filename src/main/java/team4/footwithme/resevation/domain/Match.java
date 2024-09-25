package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE match SET is_deleted = TRUE WHERE match_id = ?")
@Entity
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_team_reservation_id", nullable = false)
    private Reservation firstTeamReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_team_reservation_id", nullable = false)
    private Reservation secondTeamReservation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    @Builder
    private Match(Reservation firstTeamReservation, Reservation secondTeamReservation, MatchStatus matchStatus) {
        this.firstTeamReservation = firstTeamReservation;
        this.secondTeamReservation = secondTeamReservation;
        this.matchStatus = matchStatus;
    }

    public static Match create(Reservation firstTeamReservation, Reservation secondTeamReservation, MatchStatus matchStatus) {
        return Match.builder()
            .firstTeamReservation(firstTeamReservation)
            .secondTeamReservation(secondTeamReservation)
            .matchStatus(matchStatus)
            .build();
    }

}
