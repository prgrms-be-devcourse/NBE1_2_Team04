package team4.footwithme.chat.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TeamChatroom extends Chatroom {

    private Long teamId;

    @Builder
    private TeamChatroom(String name, Long teamId) {
        super(name);
        this.teamId = teamId;
    }

    public static TeamChatroom create(String name, Long teamId) {
        return TeamChatroom.builder()
                .name(name)
                .teamId(teamId)
                .build();
    }
}
