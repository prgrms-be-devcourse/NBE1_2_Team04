package team4.footwithme.resevation.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.member.domain.Member
import team4.footwithme.stadium.domain.Court
import team4.footwithme.team.domain.Team
import java.time.LocalDateTime

@SQLDelete(sql = "UPDATE reservation SET is_deleted = 'TRUE' WHERE reservation_id = ?")
@Entity
class Reservation : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val reservationId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    var court: Court? = null
        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null
        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team? = null
        

    var matchDate: @NotNull LocalDateTime? = null
        

    @Enumerated(EnumType.STRING)
    var reservationStatus: @NotNull ReservationStatus? = null
        

    @Enumerated(EnumType.STRING)
    var gender: @NotNull ParticipantGender? = null
        

    private constructor(
        court: Court?,
        member: Member?,
        team: Team?,
        matchDate: LocalDateTime?,
        reservationStatus: ReservationStatus?,
        gender: ParticipantGender?
    ) {
        this.court = court
        this.member = member
        this.team = team
        this.matchDate = matchDate
        this.reservationStatus = reservationStatus
        this.gender = gender
    }

    protected constructor()

    fun updateStatus(reservationStatus: ReservationStatus?) {
        this.reservationStatus = reservationStatus
    }

    class ReservationBuilder internal constructor() {
        private var court: Court? = null
        private var member: Member? = null
        private var team: Team? = null
        private var matchDate: LocalDateTime? = null
        private var reservationStatus: ReservationStatus? = null
        private var gender: ParticipantGender? = null
        fun court(court: Court?): ReservationBuilder {
            this.court = court
            return this
        }

        fun member(member: Member?): ReservationBuilder {
            this.member = member
            return this
        }

        fun team(team: Team?): ReservationBuilder {
            this.team = team
            return this
        }

        fun matchDate(matchDate: LocalDateTime?): ReservationBuilder {
            this.matchDate = matchDate
            return this
        }

        fun reservationStatus(reservationStatus: ReservationStatus?): ReservationBuilder {
            this.reservationStatus = reservationStatus
            return this
        }

        fun gender(gender: ParticipantGender?): ReservationBuilder {
            this.gender = gender
            return this
        }

        fun build(): Reservation {
            return Reservation(this.court, this.member, this.team, this.matchDate, this.reservationStatus, this.gender)
        }

        override fun toString(): String {
            return "Reservation.ReservationBuilder(court=" + this.court + ", member=" + this.member + ", team=" + this.team + ", matchDate=" + this.matchDate + ", reservationStatus=" + this.reservationStatus + ", gender=" + this.gender + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?,
            reservationStatus: ReservationStatus?,
            gender: ParticipantGender?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(reservationStatus)
                .gender(gender)
                .build()
        }

        fun createMaleReadyReservation(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.MALE)
                .build()
        }

        fun createFemaleReadyReservation(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.FEMALE)
                .build()
        }

        fun createMixedReadyReservation(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.MIXED)
                .build()
        }

        fun createMaleRecruitReservation(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(ReservationStatus.RECRUITING)
                .gender(ParticipantGender.MALE)
                .build()
        }

        fun createFemaleRecruitReservation(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(ReservationStatus.RECRUITING)
                .gender(ParticipantGender.FEMALE)
                .build()
        }

        fun createMixedRecruitReservation(
            court: Court?,
            member: Member?,
            team: Team?,
            matchDate: LocalDateTime?
        ): Reservation {
            return builder()
                .court(court)
                .member(member)
                .team(team)
                .matchDate(matchDate)
                .reservationStatus(ReservationStatus.RECRUITING)
                .gender(ParticipantGender.MIXED)
                .build()
        }

        @JvmStatic
        fun builder(): ReservationBuilder {
            return ReservationBuilder()
        }
    }
}
