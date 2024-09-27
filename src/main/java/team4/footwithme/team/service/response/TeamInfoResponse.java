package team4.footwithme.team.service.response;

import team4.footwithme.team.domain.Team;

import java.util.List;

//팀 정보
public record TeamInfoResponse(
        String name,
        String description,
        String location,
        int winCount,
        int loseCount,
        int drawCount,
        List<String> evaluation,
        int maleCount,
        int femaleCount
) {

    public static TeamInfoResponse of(Team team, List<String> evaluation, int maleCount, int femaleCount){
        return new TeamInfoResponse(
                team.getName(),
                team.getDescription(),
                team.getLocation(),
                team.getTotalRecord().getWinCount(),
                team.getTotalRecord().getLoseCount(),
                team.getTotalRecord().getDrawCount(),
                evaluation,
                maleCount,
                femaleCount
        );
    }

}
