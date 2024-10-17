package team4.footwithme.vote.domain

import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class VoteItemDate : VoteItem {
    var time: LocalDateTime? = null
        

    private constructor(vote: Vote?, time: LocalDateTime?) : super(vote) {
        validateTime(time, vote!!.endAt)
        this.time = time
    }

    protected constructor()

    private fun validateTime(time: LocalDateTime?, endAt: LocalDateTime?) {
        require(!time!!.isBefore(endAt)) { "투표 종료 시간보다 이른 시간을 선택할 수 없습니다." }
    }

    class VoteItemDateBuilder internal constructor() {
        private var vote: Vote? = null
        private var time: LocalDateTime? = null
        fun vote(vote: Vote?): VoteItemDateBuilder {
            this.vote = vote
            return this
        }

        fun time(time: LocalDateTime?): VoteItemDateBuilder {
            this.time = time
            return this
        }

        fun build(): VoteItemDate {
            return VoteItemDate(this.vote, this.time)
        }

        override fun toString(): String {
            return "VoteItemDate.VoteItemDateBuilder(vote=" + this.vote + ", time=" + this.time + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(vote: Vote?, time: LocalDateTime?): VoteItemDate {
            return builder()
                .vote(vote)
                .time(time)
                .build()
        }

        fun builder(): VoteItemDateBuilder {
            return VoteItemDateBuilder()
        }
    }
}
