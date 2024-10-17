package team4.footwithme.team.repository

interface CustomTeamRepository {
    fun countMaleByMemberId(teamId: Long?): Long?

    fun countFemaleByMemberId(teamId: Long?): Long?
}
