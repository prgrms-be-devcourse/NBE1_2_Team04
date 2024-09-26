package team4.footwithme.team.service.response;

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
) {}
