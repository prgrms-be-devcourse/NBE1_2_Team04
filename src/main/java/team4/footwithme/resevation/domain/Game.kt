package team4.footwithme.resevation.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity

@SQLDelete(sql = "UPDATE game SET is_deleted = 'TRUE' WHERE game_id = ?")
@Entity
class Game : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val gameId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_team_reservation_id", nullable = false)
    var firstTeamReservation: Reservation? = null
        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_team_reservation_id", nullable = false)
    var secondTeamReservation: Reservation? = null
        

    @Enumerated(EnumType.STRING)
    var gameStatus: @NotNull GameStatus? = null
        

    private constructor(
        firstTeamReservation: Reservation?,
        secondTeamReservation: Reservation?,
        gameStatus: GameStatus?
    ) {
        this.firstTeamReservation = firstTeamReservation
        this.secondTeamReservation = secondTeamReservation
        this.gameStatus = gameStatus
    }

    protected constructor()

    fun update(gameStatus: GameStatus?) {
        this.gameStatus = gameStatus
    }

    class GameBuilder internal constructor() {
        private var firstTeamReservation: Reservation? = null
        private var secondTeamReservation: Reservation? = null
        private var gameStatus: GameStatus? = null
        fun firstTeamReservation(firstTeamReservation: Reservation?): GameBuilder {
            this.firstTeamReservation = firstTeamReservation
            return this
        }

        fun secondTeamReservation(secondTeamReservation: Reservation?): GameBuilder {
            this.secondTeamReservation = secondTeamReservation
            return this
        }

        fun gameStatus(gameStatus: GameStatus?): GameBuilder {
            this.gameStatus = gameStatus
            return this
        }

        fun build(): Game {
            return Game(this.firstTeamReservation, this.secondTeamReservation, this.gameStatus)
        }

        override fun toString(): String {
            return "Game.GameBuilder(firstTeamReservation=" + this.firstTeamReservation + ", secondTeamReservation=" + this.secondTeamReservation + ", gameStatus=" + this.gameStatus + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(
            firstTeamReservation: Reservation?,
            secondTeamReservation: Reservation?,
            gameStatus: GameStatus?
        ): Game {
            return builder()
                .firstTeamReservation(firstTeamReservation)
                .secondTeamReservation(secondTeamReservation)
                .gameStatus(gameStatus)
                .build()
        }

        @JvmStatic
        fun builder(): GameBuilder {
            return GameBuilder()
        }
    }
}
