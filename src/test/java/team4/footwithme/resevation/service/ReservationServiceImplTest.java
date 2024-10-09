package team4.footwithme.resevation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.exception.CustomException;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.resevation.domain.*;
import team4.footwithme.resevation.repository.MercenaryRepository;
import team4.footwithme.resevation.repository.ParticipantRepository;
import team4.footwithme.resevation.repository.ReservationRepository;
import team4.footwithme.resevation.service.response.ReservationsResponse;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.repository.TeamRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ReservationServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MercenaryRepository mercenaryRepository;


    @DisplayName("투표를 통한 참여인원이 6명 이상이면 예약을 생성한다.")
    @Test
    void createReservationOverGameMember() {
        //given
        Member stadiumCreatorMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member1 = Member.create("test1@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member2 = Member.create("test2@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member5 = Member.create("test5@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member6 = Member.create("test6@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member7 = Member.create("test7@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.saveAll(List.of(stadiumCreatorMember, member1, member2, member3, member4, member5, member6, member7));
        Stadium stadium = Stadium.create(stadiumCreatorMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        //when
        reservationService.createReservation(member1.getMemberId(), court1.getCourtId(), savedTeam.getTeamId(), LocalDateTime.now().plusDays(1), List.of(member2.getMemberId(), member3.getMemberId(), member4.getMemberId(), member5.getMemberId(), member6.getMemberId(), member7.getMemberId()));
        //then
        List<Reservation> reservations = reservationRepository.findAll();
        List<Participant> participants = participantRepository.findAll();

        assertThat(reservations).hasSize(1);
        assertThat(participants).hasSize(6);
    }

    @DisplayName("투표를 통한 참여인원이 6명 이하이면 예약을 생성하고, 용병게시판을 만든다.")
    @Test
    void createReservationUnderGameMember() {
        //given
        Member stadiumCreatorMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member1 = Member.create("test1@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member2 = Member.create("test2@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.saveAll(List.of(stadiumCreatorMember, member1, member2, member3, member4));
        Stadium stadium = Stadium.create(stadiumCreatorMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        //when
        reservationService.createReservation(member1.getMemberId(), court1.getCourtId(), savedTeam.getTeamId(), LocalDateTime.now().plusDays(1), List.of(member1.getMemberId(), member2.getMemberId(), member3.getMemberId(), member4.getMemberId()));
        //then
        List<Reservation> reservations = reservationRepository.findAll();
        List<Participant> participants = participantRepository.findAll();
        List<Mercenary> mercenaries = mercenaryRepository.findAll();

        assertThat(reservations).hasSize(1)
                .extracting(Reservation::getGender)
                .containsExactly(ParticipantGender.MALE);
        assertThat(participants).hasSize(4);
        assertThat(mercenaries).hasSize(1);
    }

    @DisplayName("투표를 통한 참여인원이 6명 이상이면 예약을 생성한다. 남녀가 공존하면 혼성으로 성별을 저장한다.")
    @Test
    void createReservationOverGameMemberAndGenderMixed() {
        //given
        Member stadiumCreatorMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member1 = Member.create("test1@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member2 = Member.create("test2@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member5 = Member.create("test5@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member6 = Member.create("test6@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member7 = Member.create("test7@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.saveAll(List.of(stadiumCreatorMember, member1, member2, member3, member4, member5, member6, member7));
        Stadium stadium = Stadium.create(stadiumCreatorMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        //when
        reservationService.createReservation(member1.getMemberId(), court1.getCourtId(), savedTeam.getTeamId(), LocalDateTime.now().plusDays(1), List.of(member2.getMemberId(), member3.getMemberId(), member4.getMemberId(), member5.getMemberId(), member6.getMemberId(), member7.getMemberId()));
        //then
        List<Reservation> reservations = reservationRepository.findAll();
        List<Participant> participants = participantRepository.findAll();

        assertThat(reservations).hasSize(1)
                .extracting(Reservation::getGender)
                .containsExactly(ParticipantGender.MIXED);
        assertThat(participants).hasSize(6);
    }

    @DisplayName("투표를 통한 참여인원이 6명 이하이면 예약을 생성하고, 용병게시판을 만든다. 여성만 있을 시 성별을 여성으로 한다.")
    @Test
    void createReservationUnderGameMemberAndGenderFemale() {
        //given
        Member stadiumCreatorMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member1 = Member.create("test1@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member2 = Member.create("test2@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.saveAll(List.of(stadiumCreatorMember, member1, member2, member3, member4));
        Stadium stadium = Stadium.create(stadiumCreatorMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        //when
        reservationService.createReservation(member1.getMemberId(), court1.getCourtId(), savedTeam.getTeamId(), LocalDateTime.now().plusDays(1), List.of(member1.getMemberId(), member2.getMemberId(), member3.getMemberId(), member4.getMemberId()));
        //then
        List<Reservation> reservations = reservationRepository.findAll();
        List<Participant> participants = participantRepository.findAll();
        List<Mercenary> mercenaries = mercenaryRepository.findAll();

        assertThat(reservations).hasSize(1)
                .extracting(Reservation::getGender)
                .containsExactly(ParticipantGender.FEMALE);
        assertThat(participants).hasSize(4);
        assertThat(mercenaries).hasSize(1);
    }

    @DisplayName("READY 상태의 예약을 조회한다.")
    @Test
    void findReadyReservations() {
        Member stadiumCreatorMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(stadiumCreatorMember);
        Stadium stadium = Stadium.create(stadiumCreatorMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        courtRepository.save(court);
        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Reservation reservation1 = Reservation.builder()
                .court(court)
                .member(stadiumCreatorMember)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .reservationStatus(ReservationStatus.READY)
                .team(savedTeam)
                .gender(ParticipantGender.MALE)
                .build();
        reservationRepository.save(reservation1);

        Reservation reservation2 = Reservation.builder()
                .court(court)
                .member(stadiumCreatorMember)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .reservationStatus(ReservationStatus.READY)
                .team(savedTeam)
                .gender(ParticipantGender.MALE)
                .build();
        reservationRepository.save(reservation2);

        Slice<ReservationsResponse> readyReservations = reservationService.findReadyReservations(reservation1.getReservationId(), 0);

        assertThat(readyReservations).isNotEmpty();
        assertThat(readyReservations.getNumberOfElements()).isEqualTo(1);
    }


    @DisplayName("READY 상태가 아닌 예약을 조회하려고 하면 예외가 발생한다.")
    @Test
    void findReadyReservations_NotReadyStatus_ThrowsException() {
        Member stadiumCreatorMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(stadiumCreatorMember);
        Stadium stadium = Stadium.create(stadiumCreatorMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        courtRepository.save(court);
        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Reservation reservation = Reservation.builder()
                .court(court)
                .member(stadiumCreatorMember)
                .matchDate(LocalDateTime.now().plusDays(1))
                .reservationStatus(ReservationStatus.CONFIRMED)
                .team(savedTeam)
                .gender(ParticipantGender.MALE)
                .build();
        reservationRepository.save(reservation);

        assertThatThrownBy(() -> reservationService.findReadyReservations(reservation.getReservationId(), 0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionMessage.RESERVATION_STATUS_NOT_READY.getText());
    }

}