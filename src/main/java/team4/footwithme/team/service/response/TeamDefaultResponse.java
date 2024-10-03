package team4.footwithme.team.service.response;

import team4.footwithme.team.domain.Team;

public record TeamDefaultResponse(
        Long teamId,
        Long stadiumId,
        String name,
        String description,
        int winCount,
        int drawCount,
        int loseCount,
        String location
) {
        public static TeamDefaultResponse from(Team team) {
                return new TeamDefaultResponse(
                        team.getTeamId(),
                        team.getStadiumId(),
                        team.getName(),
                        team.getDescription(),
                        team.getTotalRecord().getWinCount(),
                        team.getTotalRecord().getDrawCount(),
                        team.getTotalRecord().getLoseCount(),
                        team.getLocation()
                );
        }

}
