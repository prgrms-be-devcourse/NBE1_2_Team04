package team4.footwithme.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.team.domain.Team
import java.util.*

@Repository
interface TeamRepository : JpaRepository<Team?, Long?>, CustomTeamRepository {
    @Query("select t from Team t where t.isDeleted = 'false' and t.teamId = :id")
    fun findByTeamId(@Param("id") teamId: Long?): Optional<Team>
}
