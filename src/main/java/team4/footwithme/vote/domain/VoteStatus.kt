package team4.footwithme.vote.domain

import lombok.Getter

@Getter
enum class VoteStatus(@JvmField val text: String) {
    OPENED("진행 중"), CLOSED("종료");
}
