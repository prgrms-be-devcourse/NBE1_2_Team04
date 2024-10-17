package team4.footwithme.chat.domain

import jakarta.persistence.Entity

@Entity
class ReservationChatroom : Chatroom {
    var reservationId: Long? = null
        

    private constructor(name: String?, reservationId: Long?) : super(name) {
        this.reservationId = reservationId
    }

    protected constructor()

    class ReservationChatroomBuilder internal constructor() {
        private var name: String? = null
        private var reservationId: Long? = null

        fun name(name: String?): ReservationChatroomBuilder {
            this.name = name
            return this
        }

        fun reservationId(reservationId: Long?): ReservationChatroomBuilder {
            this.reservationId = reservationId
            return this
        }

        fun build(): ReservationChatroom {
            return ReservationChatroom(this.name, this.reservationId)
        }

        override fun toString(): String {
            return "ReservationChatroom.ReservationChatroomBuilder(name=" + this.name + ", reservationId=" + this.reservationId + ")"
        }
    }

    companion object {
        fun create(name: String?, reservationId: Long?): ReservationChatroom {
            return builder()
                .name(name)
                .reservationId(reservationId)
                .build()
        }

        fun builder(): ReservationChatroomBuilder {
            return ReservationChatroomBuilder()
        }
    }
}
