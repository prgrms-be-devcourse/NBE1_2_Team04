package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.team.domain.Team;

import java.time.LocalDateTime;

@SQLDelete(sql = "UPDATE reservation SET is_deleted = 'TRUE' WHERE reservation_id = ?")
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    private LocalDateTime matchDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ParticipantGender gender;

    private Reservation(Court court, Member member, Team team, LocalDateTime matchDate, ReservationStatus reservationStatus, ParticipantGender gender) {
        this.court = court;
        this.member = member;
        this.team = team;
        this.matchDate = matchDate;
        this.reservationStatus = reservationStatus;
        this.gender = gender;
    }

    protected Reservation() {
    }

    public static Reservation create(Court court, Member member, Team team, LocalDateTime matchDate, ReservationStatus reservationStatus, ParticipantGender gender) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(reservationStatus)
            .gender(gender)
            .build();
    }

    public static Reservation createMaleReadyReservation(Court court, Member member, Team team, LocalDateTime matchDate) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(ReservationStatus.READY)
            .gender(ParticipantGender.MALE)
            .build();

    }

    public static Reservation createFemaleReadyReservation(Court court, Member member, Team team, LocalDateTime matchDate) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(ReservationStatus.READY)
            .gender(ParticipantGender.FEMALE)
            .build();
    }

    public static Reservation createMixedReadyReservation(Court court, Member member, Team team, LocalDateTime matchDate) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(ReservationStatus.READY)
            .gender(ParticipantGender.MIXED)
            .build();
    }

    public static Reservation createMaleRecruitReservation(Court court, Member member, Team team, LocalDateTime matchDate) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(ReservationStatus.RECRUITING)
            .gender(ParticipantGender.MALE)
            .build();

    }

    public static Reservation createFemaleRecruitReservation(Court court, Member member, Team team, LocalDateTime matchDate) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(ReservationStatus.RECRUITING)
            .gender(ParticipantGender.FEMALE)
            .build();
    }

    public static Reservation createMixedRecruitReservation(Court court, Member member, Team team, LocalDateTime matchDate) {
        return Reservation.builder()
            .court(court)
            .member(member)
            .team(team)
            .matchDate(matchDate)
            .reservationStatus(ReservationStatus.RECRUITING)
            .gender(ParticipantGender.MIXED)
            .build();
    }

    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }

    public void updateStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public Long getReservationId() {
        return this.reservationId;
    }

    public Court getCourt() {
        return this.court;
    }

    public Member getMember() {
        return this.member;
    }

    public Team getTeam() {
        return this.team;
    }

    public @NotNull LocalDateTime getMatchDate() {
        return this.matchDate;
    }

    public @NotNull ReservationStatus getReservationStatus() {
        return this.reservationStatus;
    }

    public @NotNull ParticipantGender getGender() {
        return this.gender;
    }

    public static class ReservationBuilder {
        private Court court;
        private Member member;
        private Team team;
        private LocalDateTime matchDate;
        private ReservationStatus reservationStatus;
        private ParticipantGender gender;

        ReservationBuilder() {
        }

        public ReservationBuilder court(Court court) {
            this.court = court;
            return this;
        }

        public ReservationBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ReservationBuilder team(Team team) {
            this.team = team;
            return this;
        }

        public ReservationBuilder matchDate(LocalDateTime matchDate) {
            this.matchDate = matchDate;
            return this;
        }

        public ReservationBuilder reservationStatus(ReservationStatus reservationStatus) {
            this.reservationStatus = reservationStatus;
            return this;
        }

        public ReservationBuilder gender(ParticipantGender gender) {
            this.gender = gender;
            return this;
        }

        public Reservation build() {
            return new Reservation(this.court, this.member, this.team, this.matchDate, this.reservationStatus, this.gender);
        }

        public String toString() {
            return "Reservation.ReservationBuilder(court=" + this.court + ", member=" + this.member + ", team=" + this.team + ", matchDate=" + this.matchDate + ", reservationStatus=" + this.reservationStatus + ", gender=" + this.gender + ")";
        }
    }
}
