package team4.footwithme.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

@Entity
@SQLDelete(sql = "UPDATE team_member SET is_deleted = 'TRUE' WHERE team_member_id = ?")
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamMemberRole role;

    private TeamMember(Team team, Member member, TeamMemberRole role) {
        this.team = team;
        this.member = member;
        this.role = role;
    }

    protected TeamMember() {
    }

    public static TeamMember create(Team team, Member member, TeamMemberRole role) {
        return TeamMember.builder()
            .team(team)
            .member(member)
            .role(role)
            .build();
    }

    public static TeamMember createCreator(Team team, Member member) {
        return TeamMember.builder()
            .team(team)
            .member(member)
            .role(TeamMemberRole.CREATOR)
            .build();
    }

    public static TeamMember createMember(Team team, Member member) {
        return TeamMember.builder()
            .team(team)
            .member(member)
            .role(TeamMemberRole.MEMBER)
            .build();
    }

    public static TeamMemberBuilder builder() {
        return new TeamMemberBuilder();
    }

    public Long getTeamMemberId() {
        return this.teamMemberId;
    }

    public Team getTeam() {
        return this.team;
    }

    public Member getMember() {
        return this.member;
    }

    public @NotNull TeamMemberRole getRole() {
        return this.role;
    }

    public static class TeamMemberBuilder {
        private Team team;
        private Member member;
        private TeamMemberRole role;

        TeamMemberBuilder() {
        }

        public TeamMemberBuilder team(Team team) {
            this.team = team;
            return this;
        }

        public TeamMemberBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public TeamMemberBuilder role(TeamMemberRole role) {
            this.role = role;
            return this;
        }

        public TeamMember build() {
            return new TeamMember(this.team, this.member, this.role);
        }

        public String toString() {
            return "TeamMember.TeamMemberBuilder(team=" + this.team + ", member=" + this.member + ", role=" + this.role + ")";
        }
    }
}
