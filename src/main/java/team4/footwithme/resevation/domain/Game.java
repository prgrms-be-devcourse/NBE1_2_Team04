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
@SQLDelete(sql = "UPDATE game SET is_deleted = 'TRUE' WHERE game_id = ?")
@Entity
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_team_reservation_id", nullable = false)
    private Reservation firstTeamReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_team_reservation_id", nullable = false)
    private Reservation secondTeamReservation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Builder
    private Game(Reservation firstTeamReservation, Reservation secondTeamReservation, GameStatus gameStatus) {
        this.firstTeamReservation = firstTeamReservation;
        this.secondTeamReservation = secondTeamReservation;
        this.gameStatus = gameStatus;
    }

    public static Game create(Reservation firstTeamReservation, Reservation secondTeamReservation, GameStatus gameStatus) {
        return Game.builder()
            .firstTeamReservation(firstTeamReservation)
            .secondTeamReservation(secondTeamReservation)
            .gameStatus(gameStatus)
            .build();
    }

    public void update(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

}
