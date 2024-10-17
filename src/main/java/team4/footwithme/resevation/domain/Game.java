package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

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

    private Game(Reservation firstTeamReservation, Reservation secondTeamReservation, GameStatus gameStatus) {
        this.firstTeamReservation = firstTeamReservation;
        this.secondTeamReservation = secondTeamReservation;
        this.gameStatus = gameStatus;
    }

    protected Game() {
    }

    public static Game create(Reservation firstTeamReservation, Reservation secondTeamReservation, GameStatus gameStatus) {
        return Game.builder()
            .firstTeamReservation(firstTeamReservation)
            .secondTeamReservation(secondTeamReservation)
            .gameStatus(gameStatus)
            .build();
    }

    public static GameBuilder builder() {
        return new GameBuilder();
    }

    public void update(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Long getGameId() {
        return this.gameId;
    }

    public Reservation getFirstTeamReservation() {
        return this.firstTeamReservation;
    }

    public Reservation getSecondTeamReservation() {
        return this.secondTeamReservation;
    }

    public @NotNull GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public static class GameBuilder {
        private Reservation firstTeamReservation;
        private Reservation secondTeamReservation;
        private GameStatus gameStatus;

        GameBuilder() {
        }

        public GameBuilder firstTeamReservation(Reservation firstTeamReservation) {
            this.firstTeamReservation = firstTeamReservation;
            return this;
        }

        public GameBuilder secondTeamReservation(Reservation secondTeamReservation) {
            this.secondTeamReservation = secondTeamReservation;
            return this;
        }

        public GameBuilder gameStatus(GameStatus gameStatus) {
            this.gameStatus = gameStatus;
            return this;
        }

        public Game build() {
            return new Game(this.firstTeamReservation, this.secondTeamReservation, this.gameStatus);
        }

        public String toString() {
            return "Game.GameBuilder(firstTeamReservation=" + this.firstTeamReservation + ", secondTeamReservation=" + this.secondTeamReservation + ", gameStatus=" + this.gameStatus + ")";
        }
    }
}
