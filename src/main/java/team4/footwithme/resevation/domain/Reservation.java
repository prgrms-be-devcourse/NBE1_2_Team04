package team4.footwithme.resevation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.team.domain.Team;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private Reservation(Court court, Member member, Team team, LocalDateTime matchDate, ReservationStatus reservationStatus, ParticipantGender gender) {
        this.court = court;
        this.member = member;
        this.team = team;
        this.matchDate = matchDate;
        this.reservationStatus = reservationStatus;
        this.gender = gender;
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

    public void updateStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
