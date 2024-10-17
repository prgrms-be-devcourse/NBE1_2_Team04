package team4.footwithme.team.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity

@Entity
@SQLDelete(sql = "UPDATE team_rate SET is_deleted = TRUE WHERE team_rate_id = ?")
class TeamRate : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val teamRateId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team? = null
        

    var rating: @NotNull Double = 0.0
        

    @Column(length = 100, nullable = true)
    var evaluation: String? = null
        

    //테스트 용 생성자
    constructor(team: Team?, rating: Double, evaluation: String?) {
        this.team = team
        this.rating = rating
        this.evaluation = evaluation
    }

    protected constructor()
}
