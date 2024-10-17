package team4.footwithme.resevation.domain

import lombok.Getter

@Getter
enum class GameStatus {
    PENDING,
    READY,
    IGNORE,
    PLAY,
    DONE
}
