package team4.footwithme.team.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.member.domain.Member

@Entity
@SQLDelete(sql = "UPDATE team_member SET is_deleted = 'TRUE' WHERE team_member_id = ?")
class TeamMember : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val teamMemberId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team? = null
        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null
        

    @Enumerated(EnumType.STRING)
    var role: @NotNull TeamMemberRole? = null
        

    private constructor(team: Team?, member: Member?, role: TeamMemberRole?) {
        this.team = team
        this.member = member
        this.role = role
    }

    protected constructor()

    class TeamMemberBuilder internal constructor() {
        private var team: Team? = null
        private var member: Member? = null
        private var role: TeamMemberRole? = null
        fun team(team: Team?): TeamMemberBuilder {
            this.team = team
            return this
        }

        fun member(member: Member?): TeamMemberBuilder {
            this.member = member
            return this
        }

        fun role(role: TeamMemberRole?): TeamMemberBuilder {
            this.role = role
            return this
        }

        fun build(): TeamMember {
            return TeamMember(this.team, this.member, this.role)
        }

        override fun toString(): String {
            return "TeamMember.TeamMemberBuilder(team=" + this.team + ", member=" + this.member + ", role=" + this.role + ")"
        }
    }

    companion object {
        fun create(team: Team?, member: Member?, role: TeamMemberRole?): TeamMember {
            return builder()
                .team(team)
                .member(member)
                .role(role)
                .build()
        }

        @JvmStatic
        fun createCreator(team: Team?, member: Member?): TeamMember {
            return builder()
                .team(team)
                .member(member)
                .role(TeamMemberRole.CREATOR)
                .build()
        }

        @JvmStatic
        fun createMember(team: Team?, member: Member?): TeamMember {
            return builder()
                .team(team)
                .member(member)
                .role(TeamMemberRole.MEMBER)
                .build()
        }

        fun builder(): TeamMemberBuilder {
            return TeamMemberBuilder()
        }
    }
}
