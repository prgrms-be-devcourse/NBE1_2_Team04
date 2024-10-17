package team4.footwithme.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team4.footwithme.team.domain.Team
import team4.footwithme.team.domain.TeamRate

@Repository
interface TeamRateRepository : JpaRepository<TeamRate?, Long?> {
    fun findEvaluationsByTeam(team: Team?): List<TeamRate?>?
}
