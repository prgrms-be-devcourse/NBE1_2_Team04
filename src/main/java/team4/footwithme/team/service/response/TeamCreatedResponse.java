package team4.footwithme.team.service.response;

import team4.footwithme.stadium.domain.Stadium;

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
        )
{}
