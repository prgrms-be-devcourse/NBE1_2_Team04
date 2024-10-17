package team4.footwithme.team.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.chat.service.event.TeamDeletedEvent
import team4.footwithme.chat.service.event.TeamPublishedEvent
import team4.footwithme.member.domain.Member
import team4.footwithme.member.repository.MemberRepository
import team4.footwithme.team.domain.Team
import team4.footwithme.team.domain.TeamMember
import team4.footwithme.team.domain.TeamMemberRole
import team4.footwithme.team.domain.TotalRecord
import team4.footwithme.team.repository.TeamMemberRepository
import team4.footwithme.team.repository.TeamRateRepository
import team4.footwithme.team.repository.TeamRepository
import team4.footwithme.team.service.request.TeamDefaultServiceRequest
import team4.footwithme.team.service.response.TeamDefaultResponse
import team4.footwithme.team.service.response.TeamInfoResponse

@Service
class TeamServiceImpl(
    private val teamRepository: TeamRepository,
    private val teamRateRepository: TeamRateRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val memberRepository: MemberRepository,
    private val publisher: ApplicationEventPublisher
) : TeamService {
    @Transactional
    override fun createTeam(dto: TeamDefaultServiceRequest?, member: Member?): TeamDefaultResponse {
        /**
         * # 1.
         * chatRoomId는 일단 가짜 id 넣어놓고,
         * 채팅방 생기면 해당 id 적용
         */
        val stadiumId: Long? = null

        //TotalRecord 초기값으로 생성
        val totalRecord: TotalRecord = TotalRecord.Companion.builder().build()

        //dto -> entity
        val entity: Team = Team.Companion.create(
            stadiumId,
            dto!!.name,
            dto.description,
            totalRecord.winCount,
            totalRecord.drawCount,
            totalRecord.loseCount,
            dto.location
        )
        val createdTeam = teamRepository.save(entity)

        //채팅방 생성 이벤트 실행
        publisher.publishEvent(TeamPublishedEvent(createdTeam.name, createdTeam.teamId))

        teamMemberRepository.save<TeamMember>(TeamMember.Companion.createCreator(createdTeam, member))

        return TeamDefaultResponse.Companion.from(createdTeam)
    }


    @Transactional(readOnly = true)
    override fun getTeamInfo(teamId: Long): TeamInfoResponse {
        //팀 정보

        val teamEntity = findTeamByIdOrThrowException(teamId)

        //팀 평가 ->  List
        val teamRates = teamRateRepository.findEvaluationsByTeam(teamEntity)
        val evaluations: MutableList<String?> = ArrayList()

        for (teamRate in teamRates!!) {
            evaluations.add(teamRate?.evaluation)
        }

        val maleCount = teamRepository.countMaleByMemberId(teamId)
        val femaleCount = teamRepository.countFemaleByMemberId(teamId)

        return TeamInfoResponse.Companion.of(
            teamEntity,
            evaluations,
            maleCount,
            femaleCount
        )
    }

    @Transactional
    override fun updateTeamInfo(teamId: Long, dto: TeamDefaultServiceRequest?, member: Member?): TeamDefaultResponse {
        //변경할 팀 id로 검색
        val teamEntity = findTeamByIdOrThrowException(teamId)
        //현재 유저 정보 검색
        val teamMember = findTeamMemberByIdOrThrowException(teamId, member!!.memberId)
        //권한 정보
        checkAuthority(teamId, teamMember)

        //entity에 수정된 값 적용
        if (dto!!.name != null) {
            teamEntity.updateName(dto.name)
        }
        if (dto.description != null) {
            teamEntity.updateDescription(dto.description)
        }
        if (dto.location != null) {
            teamEntity.updateLocation(dto.location)
        }

        //바뀐 Team값 반환
        return TeamDefaultResponse.Companion.from(teamEntity)
    }

    @Transactional
    override fun deleteTeam(teamId: Long, member: Member?): Long {
        //삭제할 팀 탐색
        val teamEntity = findTeamByIdOrThrowException(teamId)
        //현재 유저 정보 검색
        val teamMember = findTeamMemberByIdOrThrowException(teamId, member!!.memberId)
        //권한 정보
        checkAuthority(teamId, teamMember)

        teamRepository.delete(teamEntity)
        // 채팅방 삭제 이벤트 실행
        publisher.publishEvent(TeamDeletedEvent(teamId))
        return teamId
    }


    fun findTeamByIdOrThrowException(id: Long): Team {
        val team = teamRepository.findByTeamId(id)
            .orElseThrow { IllegalArgumentException("해당 팀이 존재하지 않습니다.") }
        return team
    }

    fun findTeamMemberByIdOrThrowException(teamId: Long?, memberId: Long?): TeamMember {
        val teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 팀원입니다.") }
        return teamMember
    }

    fun checkAuthority(teamId: Long, teamMember: TeamMember) {
        require(!(teamMember.team?.teamId !== teamId || teamMember.role != TeamMemberRole.CREATOR)) { "접근 권한이 없습니다." }
    }
}
