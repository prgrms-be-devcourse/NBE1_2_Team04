package team4.footwithme.team.service.response;

import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.team.domain.Team;

public record TeamCreatedResponse(
        Long teamId,
        Long stadiumId,
        Long chatRoomId,
        String name,
        String description,
        int winCount,
        int drawCount,
        int loseCount,
        String location
) {
        public static TeamCreatedResponse of(Team team) {
                return new TeamCreatedResponse(
                        team.getTeamId(),
                        team.getStadiumId(),
                        team.getChatRoomId(),
                        team.getName(),
                        team.getDescription(),
                        team.getTotalRecord().getWinCount(),
                        team.getTotalRecord().getDrawCount(),
                        team.getTotalRecord().getLoseCount(),
                        team.getLocation()
                );
        }

}
