package team4.footwithme.team.service.response

import team4.footwithme.team.domain.Team

//팀 정보
@JvmRecord
data class TeamInfoResponse(
    @JvmField val name: String?,
    val description: String?,
    val location: String?,
    @JvmField val winCount: Int,
    val loseCount: Int,
    val drawCount: Int,
    @JvmField val evaluation: List<String?>,
    @JvmField val maleCount: Long?,
    @JvmField val femaleCount: Long?
) {
    companion object {
        fun of(team: Team, evaluation: List<String?>, maleCount: Long?, femaleCount: Long?): TeamInfoResponse {
            return TeamInfoResponse(
                team.name,
                team.description,
                team.location,
                team.totalRecord!!.winCount,
                team.totalRecord!!.loseCount,
                team.totalRecord!!.drawCount,
                evaluation,
                maleCount,
                femaleCount
            )
        }
    }
}
