package team4.footwithme.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Position;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.api.response.TeamAddResponse;
import team4.footwithme.team.repository.TeamRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamServiceImplTest {
    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @Transactional
    @DisplayName("팀 생성")
    void createTeam() {
        //given
        TeamAddResponse dto = new TeamAddResponse("팀명", "팀 설명", "선호지역");
        teamService.createTeam(dto);

        //when
        long count = teamRepository.count();
        //then
        assertThat(count).isEqualTo(1);
    }
}