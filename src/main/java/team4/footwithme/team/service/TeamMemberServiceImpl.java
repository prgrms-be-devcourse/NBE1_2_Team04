package team4.footwithme.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamMemberServiceImpl implements TeamMemberService{


    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    @Transactional
    public List<TeamResponse> addTeamMembers(Long teamId, TeamMemberServiceRequest request) {
        //팀원 추가할 팀 찾기
        Team team = findTeamByIdOrThrowException(teamId);

        //return할 DTO
        List<TeamResponse> addList = new ArrayList<>();

        //member 추가
        for(String email : request.emails()){
            Member member = memberRepository.findByEmail(email).orElse(null);
            if(member == null){
                continue;
            }
            // TODO :: 이전에 참가했다 탈퇴한 멤버면 is_delete = false 변경 or 중복 참여 금지
            TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, member.getMemberId());
            //해당 멤버가 팀에 이미 존재 할 경우
            if(teamMember != null){
                //추가되었지만 삭제되었던 멤버
                if(teamMember.getIsDeleted().equals(IsDeleted.TRUE)){
                    teamMember.restoreTeamMember();
                }else{ //이미 등록되어있는 멤버
                    continue;
                }
            }else{
                teamMember = TeamMember.create(team, member, TeamMemberRole.MEMBER);
            }
            addList.add(TeamResponse.of(teamMemberRepository.save(teamMember)));
        }
        return addList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamMembers(Long teamId) {
        //팀 찾기
        Team team = findTeamByIdOrThrowException(teamId);

        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);
        List<TeamResponse> membersInfo = new ArrayList<>();

        for(TeamMember teamMember : teamMembers){
            membersInfo.add(TeamResponse.of(teamMember));
        }
        return membersInfo;
    }

    @Override
    @Transactional
    public Long deleteTeamMembers(Long teamId, Long teamMemberId, Member member) {
        //팀 멤버 찾기
        TeamMember teamMember = findTeamMemberByIdOrThrowException(teamMemberId);
        //유저 정보
        TeamMember curUser = findByTeamIdAndMemberIdOrThrowException(teamId, member.getMemberId());
        // TODO :: 본인 or Creator만 삭제 권한
        if(curUser.getRole() != TeamMemberRole.CREATOR && teamMember.getMember().getMemberId()!=member.getMemberId()){
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        teamMemberRepository.delete(teamMember);
        return teamMemberId;
    }


    public Team findTeamByIdOrThrowException(long id){
        Team team = teamRepository.findByTeamId(id);
        if(team == null) {
            throw new IllegalArgumentException("해당 팀이 존재하지 않습니다.");
        }
        return team;
    }

    public TeamMember findTeamMemberByIdOrThrowException(long id){
        TeamMember teamMember = teamMemberRepository.findByTeamMemberId(id);
        if(teamMember == null) {
            throw new IllegalArgumentException("존재하지 않는 팀원입니다.");
        }
        return teamMember;
    }

    public TeamMember findByTeamIdAndMemberIdOrThrowException(long teamId, long memberId){
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId);
        if(teamMember == null) {
            throw new IllegalArgumentException("존재하지 않는 팀원입니다.");
        }
        return teamMember;
    }

}
