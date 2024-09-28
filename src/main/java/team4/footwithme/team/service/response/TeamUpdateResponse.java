package team4.footwithme.team.service.response;

import team4.footwithme.team.domain.Team;

public record TeamUpdateResponse(
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
    public static TeamUpdateResponse of(Team team) {
        return new TeamUpdateResponse(
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
