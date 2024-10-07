package team4.footwithme.team.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.chat.domain.ChatMember;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.domain.TeamChatroom;
import team4.footwithme.chat.repository.ChatMemberRepository;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.repository.RedisChatroomRepository;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class TeamMemberServiceImplTest extends IntegrationTestSupport {
    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private RedisChatroomRepository redisChatroomRepository;
    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @BeforeEach
    void setUp() {
        //팀 생성
        team = Team.create(111L, "name","테스트 팀 설명", 0, 0, 0, "서울");
        teamRepository.saveAndFlush(team);

        //팀장용 멤버 생성
        memberRepository.save(
            Member.create("teamLeader@gmail.com", "123456", "팀장", "010-1111-1111",
                    LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );

        // 멤버 생성
        memberRepository.save(
            Member.create("member01@gmail.com", "123456", "남팀원01", "010-1111-1111",
                    LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );
        memberRepository.save(
            Member.create("member02@gmail.com", "123456", "남팀원02", "010-1111-1111",
                    LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER,TermsAgreed.AGREE)
        );
        memberRepository.save(
            Member.create("member03@gmail.com", "123456", "여팀원01", "010-1111-1111",
                    LoginProvider.ORIGINAL,"test", Gender.FEMALE, MemberRole.USER,TermsAgreed.AGREE)
        );

    }

    @Test
    @DisplayName("팀원 추가")
    void addTeamMember() {
        //given
        Team team = teamRepository.save(Team.create(null,"팀명","팀 설명", 0, 0, 0,"선호지역"));
        List<String> emails = new ArrayList<>();
        List<Member> members = memberRepository.findAll();
        for(Member member : members) {
            emails.add(member.getEmail());
        }
        TeamMemberServiceRequest request = new TeamMemberServiceRequest(emails);
        //해당 팀의 채팅방 생성
        Chatroom chatroom = chatroomRepository.save(TeamChatroom.create(team.getName(), team.getTeamId()));
        // redis Hash에 저장
        redisChatroomRepository.createChatRoom(chatroom);

        //when
        List<TeamResponse> response = teamMemberService.addTeamMembers(team.getTeamId(),request);

        //then
        assertThat(response.size()).isEqualTo(4);
        assertThat(response.get(0).name()).isEqualTo("팀장");
        assertThat(response.get(0).role()).isEqualTo(TeamMemberRole.MEMBER);
        assertThat(response.get(1).name()).isEqualTo("남팀원01");
        assertThat(response.get(1).role()).isEqualTo(TeamMemberRole.MEMBER);
    }

    @Test
    @DisplayName("팀원 조회")
    public void getTeamMembers() {
        //given
        //팀 정보 저장
        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member01 = memberRepository.findByEmail("member01@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member02 = memberRepository.findByEmail("member02@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member03 = memberRepository.findByEmail("member03@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Team team = teamRepository.save(Team.create(null,"팀명","팀 설명", 0, 0, 0,"선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leader));
        teamMemberRepository.save(TeamMember.createMember(team, member01));
        teamMemberRepository.save(TeamMember.createMember(team, member02));
        teamMemberRepository.save(TeamMember.createMember(team, member03));

        //when
        List<TeamResponse> response = teamMemberService.getTeamMembers(team.getTeamId());

        //then
        assertThat(response.size()).isEqualTo(4);
        assertThat(response.get(0).name()).isEqualTo("팀장");
        assertThat(response.get(0).role()).isEqualTo(TeamMemberRole.CREATOR);
        assertThat(response.get(1).name()).isEqualTo("남팀원01");
        assertThat(response.get(1).role()).isEqualTo(TeamMemberRole.MEMBER);
    }

    @Test
    @DisplayName("팀원 삭제_팀장")
    public void deleteTeamMemberByCreator(){
        //given
        //팀 정보 저장
        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member = memberRepository.findByEmail("member01@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Team team = teamRepository.save(Team.create(null,"팀명","팀 설명", 0, 0, 0,"선호지역"));
        TeamMember creator = teamMemberRepository.save(TeamMember.createCreator(team, leader));
        TeamMember user = teamMemberRepository.save(TeamMember.createMember(team, member));
        //해당 팀의 채팅방 생성
        Chatroom chatroom = chatroomRepository.save(TeamChatroom.create(team.getName(), team.getTeamId()));
        redisChatroomRepository.createChatRoom(chatroom);
        //채팅방에 멤버 추가
        chatMemberRepository.save(ChatMember.create(leader, chatroom));
        chatMemberRepository.save(ChatMember.create(member, chatroom));

        //when -- 본인 탈퇴
        teamMemberService.deleteTeamMemberByCreator(team.getTeamId(),user.getTeamMemberId(),leader);
        TeamMember result = teamMemberRepository.findById(user.getTeamMemberId())
                .orElse(null);
        //then
        assertThat(teamMemberRepository.findAll()).hasSize(2);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("팀원 삭제_본인")
    public void deleteTeamMember(){
        //given
        //팀 정보 저장
        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member = memberRepository.findByEmail("member01@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Team team = teamRepository.save(Team.create(null,"팀명","팀 설명", 0, 0, 0,"선호지역"));
        TeamMember creator = teamMemberRepository.save(TeamMember.createCreator(team, leader));
        TeamMember user = teamMemberRepository.save(TeamMember.createMember(team, member));
        //해당 팀의 채팅방 생성
        Chatroom chatroom = chatroomRepository.save(TeamChatroom.create(team.getName(), team.getTeamId()));
        // redis Hash에 저장
        redisChatroomRepository.createChatRoom(chatroom);
        //채팅방에 멤버 추가
        chatMemberRepository.save(ChatMember.create(leader, chatroom));
        chatMemberRepository.save(ChatMember.create(member, chatroom));

        //when -- 본인 탈퇴
        teamMemberService.deleteTeamMember(team.getTeamId(),member);
        TeamMember result = teamMemberRepository.findById(user.getTeamMemberId())
                .orElse(null);
        //then
        assertThat(teamMemberRepository.findAll()).hasSize(2);
        assertThat(result).isNull();
    }
}