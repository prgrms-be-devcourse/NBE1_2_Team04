package team4.footwithme.vote.domain

import jakarta.persistence.*

@Table(
    indexes = [Index(name = "idx_choice_member_id", columnList = "memberId"), Index(
        name = "idx_choice_vote_item_id",
        columnList = "voteItemId"
    )]
)
@Entity
class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val choiceId: Long? = null

    var memberId: Long? = null
        

    var voteItemId: Long? = null
        

    private constructor(memberId: Long?, voteItemId: Long?) {
        this.memberId = memberId
        this.voteItemId = voteItemId
    }

    protected constructor()

    class ChoiceBuilder internal constructor() {
        private var memberId: Long? = null
        private var voteItemId: Long? = null
        fun memberId(memberId: Long?): ChoiceBuilder {
            this.memberId = memberId
            return this
        }

        fun voteItemId(voteItemId: Long?): ChoiceBuilder {
            this.voteItemId = voteItemId
            return this
        }

        fun build(): Choice {
            return Choice(this.memberId, this.voteItemId)
        }

        override fun toString(): String {
            return "Choice.ChoiceBuilder(memberId=" + this.memberId + ", voteItemId=" + this.voteItemId + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(memberId: Long?, voteItemId: Long?): Choice {
            return builder()
                .memberId(memberId)
                .voteItemId(voteItemId)
                .build()
        }

        fun builder(): ChoiceBuilder {
            return ChoiceBuilder()
        }
    }
}
