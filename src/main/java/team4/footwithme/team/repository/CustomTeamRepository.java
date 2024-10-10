package team4.footwithme.team.repository;

public interface CustomTeamRepository {
    Long countMaleByMemberId(Long teamId);

    Long countFemaleByMemberId(Long teamId);
}
