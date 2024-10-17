package team4.footwithme.vote.domain

import jakarta.persistence.*

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Entity
open class VoteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val voteItemId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    var vote: Vote? = null
        

    constructor(vote: Vote?) {
        this.vote = vote
        vote!!.addChoice(this)
    }

    protected constructor()
}
