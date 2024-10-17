package team4.footwithme.chat.domain

import jakarta.persistence.Entity

@Entity
class TeamChatroom : Chatroom {
    var teamId: Long? = null
        

    private constructor(name: String?, teamId: Long?) : super(name) {
        this.teamId = teamId
    }

    protected constructor()

    class TeamChatroomBuilder internal constructor() {
        private var name: String? = null
        private var teamId: Long? = null

        fun name(name: String?): TeamChatroomBuilder {
            this.name = name
            return this
        }

        fun teamId(teamId: Long?): TeamChatroomBuilder {
            this.teamId = teamId
            return this
        }

        fun build(): TeamChatroom {
            return TeamChatroom(this.name, this.teamId)
        }

        override fun toString(): String {
            return "TeamChatroom.TeamChatroomBuilder(name=" + this.name + ", teamId=" + this.teamId + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(name: String?, teamId: Long?): TeamChatroom {
            return builder()
                .name(name)
                .teamId(teamId)
                .build()
        }

        fun builder(): TeamChatroomBuilder {
            return TeamChatroomBuilder()
        }
    }
}
