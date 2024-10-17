package team4.footwithme.vote.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Table(
    indexes = [Index(name = "idx_vote_team_id", columnList = "teamId"), Index(
        name = "idx_vote_member_id",
        columnList = "memberId"
    )]
)
@SQLDelete(sql = "UPDATE vote SET is_deleted = 'TRUE' WHERE vote_id = ?")
@Entity
class Vote : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val voteId: Long? = null

    var memberId: @NotNull Long? = null
        

    var teamId: @NotNull Long? = null
        

    @Column(length = 50)
    var title: @NotNull String? = null
        

    var endAt: @NotNull LocalDateTime? = null
        

    @Enumerated(EnumType.STRING)
    var voteStatus: @NotNull VoteStatus? = null
        

    @OneToMany(mappedBy = "vote")
    private val voteItems: MutableList<VoteItem?> = ArrayList()

    private constructor(memberId: Long?, teamId: Long?, title: String?, endAt: LocalDateTime?) {
        this.memberId = memberId
        this.teamId = teamId
        this.title = title
        this.endAt = endAt
        this.voteStatus = VoteStatus.OPENED
    }

    protected constructor()

    fun addChoice(voteItem: VoteItem?) {
        voteItems.add(voteItem)
    }

    fun updateVoteStatusToClose() {
        this.voteStatus = VoteStatus.CLOSED
    }

    fun update(updateTitle: String?, updateEndAt: LocalDateTime?, memberId: Long?) {
        checkWriterFromMemberId(memberId)
        this.title = updateTitle
        this.endAt = updateEndAt
    }

    fun checkWriterFromMemberId(memberId: Long?) {
        require(!isNotWriter(memberId)) { "투표 작성자가 아닙니다." }
    }

    private fun isNotWriter(memberId: Long?): Boolean {
        return this.memberId != memberId
    }

    val instantEndAt: Instant
        get() = endAt!!.atZone(ZoneId.systemDefault()).toInstant()

    fun getVoteItems(): List<VoteItem?> {
        return this.voteItems
    }

    class VoteBuilder internal constructor() {
        private var memberId: Long? = null
        private var teamId: Long? = null
        private var title: String? = null
        private var endAt: LocalDateTime? = null
        fun memberId(memberId: Long?): VoteBuilder {
            this.memberId = memberId
            return this
        }

        fun teamId(teamId: Long?): VoteBuilder {
            this.teamId = teamId
            return this
        }

        fun title(title: String?): VoteBuilder {
            this.title = title
            return this
        }

        fun endAt(endAt: LocalDateTime?): VoteBuilder {
            this.endAt = endAt
            return this
        }

        fun build(): Vote {
            return Vote(this.memberId, this.teamId, this.title, this.endAt)
        }

        override fun toString(): String {
            return "Vote.VoteBuilder(memberId=" + this.memberId + ", teamId=" + this.teamId + ", title=" + this.title + ", endAt=" + this.endAt + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(memberId: Long?, teamId: Long?, title: String?, endAt: LocalDateTime?): Vote {
            validateEndAt(endAt)
            return builder()
                .memberId(memberId)
                .teamId(teamId)
                .title(title)
                .endAt(endAt)
                .build()
        }

        private fun validateEndAt(endAt: LocalDateTime?) {
            require(!endAt!!.isBefore(LocalDateTime.now())) { "투표 종료일은 현재 시간 이후여야 합니다." }
        }

        @JvmStatic
        fun builder(): VoteBuilder {
            return VoteBuilder()
        }
    }
}
