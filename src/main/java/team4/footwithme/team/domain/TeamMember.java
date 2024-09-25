package team4.footwithme.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

@Getter
@Entity
@SQLDelete(sql = "UPDATE team_member SET is_deleted = TRUE WHERE team_member_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private TeamMember(Team team, Member member, TeamMemberRole role) {
        this.team = team;
        this.member = member;
        this.role = role;
    }

    public static TeamMember create(Team team, Member member, TeamMemberRole role) {
        return TeamMember.builder()
            .team(team)
            .member(member)
            .role(role)
            .build();
    }

}
