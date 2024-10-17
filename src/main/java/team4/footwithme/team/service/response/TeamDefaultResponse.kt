package team4.footwithme.team.service.response

import team4.footwithme.team.domain.Team

@JvmRecord
data class TeamDefaultResponse(
    val teamId: Long?,
    val stadiumId: Long?,
    @JvmField val name: String?,
    @JvmField val description: String?,
    val winCount: Int,
    val drawCount: Int,
    val loseCount: Int,
    @JvmField val location: String?
) {
    companion object {
        fun from(team: Team): TeamDefaultResponse {
            return TeamDefaultResponse(
                team.teamId,
                team.stadiumId,
                team.name,
                team.description,
                team.totalRecord!!.winCount,
                team.totalRecord!!.drawCount,
                team.totalRecord!!.loseCount,
                team.location
            )
        }
    }
}
