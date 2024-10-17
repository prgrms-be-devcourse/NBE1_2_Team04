package team4.footwithme.vote.domain

import jakarta.persistence.Entity

@Entity
class VoteItemLocate : VoteItem {
    var courtId: Long? = null
        

    private constructor(vote: Vote?, courtId: Long?) : super(vote) {
        this.courtId = courtId
    }

    protected constructor()

    class VoteItemLocateBuilder internal constructor() {
        private var vote: Vote? = null
        private var courtId: Long? = null
        fun vote(vote: Vote?): VoteItemLocateBuilder {
            this.vote = vote
            return this
        }

        fun courtId(courtId: Long?): VoteItemLocateBuilder {
            this.courtId = courtId
            return this
        }

        fun build(): VoteItemLocate {
            return VoteItemLocate(this.vote, this.courtId)
        }

        override fun toString(): String {
            return "VoteItemLocate.VoteItemLocateBuilder(vote=" + this.vote + ", courtId=" + this.courtId + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(vote: Vote?, courtId: Long?): VoteItemLocate {
            return builder()
                .vote(vote)
                .courtId(courtId)
                .build()
        }

        fun builder(): VoteItemLocateBuilder {
            return VoteItemLocateBuilder()
        }
    }
}
