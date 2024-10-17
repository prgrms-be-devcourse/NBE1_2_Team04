package team4.footwithme.team.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity

@SQLDelete(sql = "UPDATE team SET is_deleted = 'TRUE' WHERE team_id = ?")
@Entity
class Team : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var teamId: Long? = null
        

    @Column(nullable = true)
    var stadiumId: Long? = null
        

    @Column(length = 50)
    var name: @NotNull String? = null
        

    @Column(length = 200, nullable = true)
    var description: String? = null
        

    @Embedded
    var totalRecord: TotalRecord? = null
        

    @Column(length = 100, nullable = true)
    var location: String? = null
        

    private constructor(
        teamId: Long?,
        stadiumId: Long?,
        name: String?,
        description: String?,
        totalRecord: TotalRecord?,
        location: String?
    ) {
        this.teamId = teamId
        this.stadiumId = stadiumId
        this.name = name
        this.description = description
        this.totalRecord = totalRecord
        this.location = location
    }

    protected constructor()

    fun updateName(name: @NotNull String?) {
        this.name = name
    }

    fun updateDescription(description: String?) {
        this.description = description
    }

    fun updateLocation(location: String?) {
        this.location = location
    }

    class TeamBuilder internal constructor() {
        private var teamId: Long? = null
        private var stadiumId: Long? = null
        private var name: String? = null
        private var description: String? = null
        private var totalRecord: TotalRecord? = null
        private var location: String? = null
        fun teamId(teamId: Long?): TeamBuilder {
            this.teamId = teamId
            return this
        }

        fun stadiumId(stadiumId: Long?): TeamBuilder {
            this.stadiumId = stadiumId
            return this
        }

        fun name(name: String?): TeamBuilder {
            this.name = name
            return this
        }

        fun description(description: String?): TeamBuilder {
            this.description = description
            return this
        }

        fun totalRecord(totalRecord: TotalRecord?): TeamBuilder {
            this.totalRecord = totalRecord
            return this
        }

        fun location(location: String?): TeamBuilder {
            this.location = location
            return this
        }

        fun build(): Team {
            return Team(this.teamId, this.stadiumId, this.name, this.description, this.totalRecord, this.location)
        }

        override fun toString(): String {
            return "Team.TeamBuilder(teamId=" + this.teamId + ", stadiumId=" + this.stadiumId + ", name=" + this.name + ", description=" + this.description + ", totalRecord=" + this.totalRecord + ", location=" + this.location + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(
            stadiumId: Long?,
            name: String?,
            description: String?,
            winCount: Int,
            drawCount: Int,
            loseCount: Int,
            location: String?
        ): Team {
            return builder()
                .stadiumId(stadiumId)
                .name(name)
                .description(description)
                .totalRecord(TotalRecord.Companion.builder().build())
                .location(location)
                .build()
        }

        fun builder(): TeamBuilder {
            return TeamBuilder()
        }
    }
}
