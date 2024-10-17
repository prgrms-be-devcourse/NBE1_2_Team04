package team4.footwithme.chat.domain;

import jakarta.persistence.Entity;

@Entity
public class TeamChatroom extends Chatroom {

    private Long teamId;

    private TeamChatroom(String name, Long teamId) {
        super(name);
        this.teamId = teamId;
    }

    protected TeamChatroom() {
    }

    public static TeamChatroom create(String name, Long teamId) {
        return TeamChatroom.builder()
            .name(name)
            .teamId(teamId)
            .build();
    }

    public static TeamChatroomBuilder builder() {
        return new TeamChatroomBuilder();
    }

    public Long getTeamId() {
        return this.teamId;
    }

    public static class TeamChatroomBuilder {
        private String name;
        private Long teamId;

        TeamChatroomBuilder() {
        }

        public TeamChatroomBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TeamChatroomBuilder teamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public TeamChatroom build() {
            return new TeamChatroom(this.name, this.teamId);
        }

        public String toString() {
            return "TeamChatroom.TeamChatroomBuilder(name=" + this.name + ", teamId=" + this.teamId + ")";
        }
    }
}
