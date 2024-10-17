package team4.footwithme.resevation.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.member.domain.Member

@SQLDelete(sql = "UPDATE participant SET is_deleted = 'TRUE' WHERE participant_id = ?")
@Entity
class Participant : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val participantId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    var reservation: Reservation? = null
        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null
        

    @Enumerated(EnumType.STRING)
    var participantRole: @NotNull ParticipantRole? = null
        

    private constructor(reservation: Reservation?, member: Member?, participantRole: ParticipantRole?) {
        this.reservation = reservation
        this.member = member
        this.participantRole = participantRole
    }

    protected constructor()

    fun updateRole(participantRole: ParticipantRole?) {
        this.participantRole = participantRole
    }

    class ParticipantBuilder internal constructor() {
        private var reservation: Reservation? = null
        private var member: Member? = null
        private var participantRole: ParticipantRole? = null
        fun reservation(reservation: Reservation?): ParticipantBuilder {
            this.reservation = reservation
            return this
        }

        fun member(member: Member?): ParticipantBuilder {
            this.member = member
            return this
        }

        fun participantRole(participantRole: ParticipantRole?): ParticipantBuilder {
            this.participantRole = participantRole
            return this
        }

        fun build(): Participant {
            return Participant(this.reservation, this.member, this.participantRole)
        }

        override fun toString(): String {
            return "Participant.ParticipantBuilder(reservation=" + this.reservation + ", member=" + this.member + ", participantRole=" + this.participantRole + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(reservation: Reservation?, member: Member?, participantRole: ParticipantRole?): Participant {
            return builder()
                .reservation(reservation)
                .member(member)
                .participantRole(participantRole)
                .build()
        }

        fun builder(): ParticipantBuilder {
            return ParticipantBuilder()
        }
    }
}
