package team4.footwithme.resevation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.resevation.domain.*;
import team4.footwithme.resevation.repository.JKGameRepository;
import team4.footwithme.resevation.repository.JKMercenaryRepository;
import team4.footwithme.resevation.repository.JKParticipantRepository;
import team4.footwithme.resevation.repository.ReservationRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class JKReservationServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private JKReservationService jkReservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JKParticipantRepository participantRepository;

    @Autowired
    private JKMercenaryRepository mercenaryRepository;

    @Autowired
    private JKGameRepository gameRepository;

    @DisplayName("예약을 취소한다.")
    @Test
    void deleteReservation(){
        //given
        Member manager = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@naver.com", "!test1234", "test3", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@naver.com", "!test1234", "test4", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member5 = Member.create("test5@naver.com", "!test1234", "test5", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member6 = Member.create("test6@naver.com", "!test1234", "test6", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member7 = Member.create("test7@naver.com", "!test1234", "test7", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        Member member8 = Member.create("test8@naver.com", "!test1234", "test8", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member merchant = Member.create("test2@naver.com", "!test1234", "test2", "010-2234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.MERCHANT, TermsAgreed.AGREE);
        Stadium stadium = Stadium.create(merchant, "스타 구장", "인천 송도", "010-2234-1234", "깔끔 쾌적", 36, 36);
        Team team = Team.create(stadium.getStadiumId(), "한화", "이길 수 있을까", 5, 10, 10, "인천");
        Team team2 = Team.create(stadium.getStadiumId(), "삼성", "이길 수 있을까", 10, 10, 5, "인천");

        Court court = Court.create(stadium, "A번 구장", "잔디 구장", BigDecimal.valueOf(5000));

        Reservation reservation = Reservation.create(court, manager, team, LocalDateTime.of(2024, 10, 20, 12,00,00), ReservationStatus.RECRUITING, Gender.MALE);
        Reservation reservation2 = Reservation.create(court, manager, team2, LocalDateTime.of(2024, 10, 20, 12,00,00), ReservationStatus.RECRUITING, Gender.MALE);


        Participant participant1 = Participant.create(reservation, manager, ParticipantRole.MEMBER);
        Participant participant2 = Participant.create(reservation, member3, ParticipantRole.MEMBER);
        Participant participant3 = Participant.create(reservation, member4, ParticipantRole.MEMBER);
        Participant participant4 = Participant.create(reservation, member5, ParticipantRole.MEMBER);
        Participant participant5 = Participant.create(reservation, member6, ParticipantRole.MEMBER);
        Participant participant6 = Participant.create(reservation, member7, ParticipantRole.MEMBER);

        Mercenary mercenary = Mercenary.create(reservation,"용병 구합니다");

        Game game = Game.create(reservation, reservation2, GameStatus.READY);

        memberRepository.saveAll(List.of(manager, merchant, member3, member4, member5, member6, member7, member8));
        stadiumRepository.save(stadium);
        teamRepository.saveAll(List.of(team, team2));
        courtRepository.save(court);
        reservationRepository.saveAll(List.of(reservation, reservation2));
        participantRepository.saveAll(List.of(participant1, participant2, participant3, participant4, participant5, participant6));
        mercenaryRepository.save(mercenary);
        gameRepository.save(game);

        //when
        Long response = jkReservationService.deleteReservation(reservation.getReservationId(), manager);

        //then
        assertThat(response).isEqualTo(reservation.getReservationId());

        List<Participant> participants = participantRepository.findAllByReservationId(reservation.getReservationId());
        List<Game> games = gameRepository.findAllByReservationId(reservation.getReservationId());
        List<Mercenary> mercenaries = mercenaryRepository.findAllMercenaryByReservationId(reservation.getReservationId());

        assertThat(participants).isEmpty();
        assertThat(games).isEmpty();
        assertThat(mercenaries).isEmpty();
    }

    @DisplayName("예약을 삭제할 때 일치하지 않는 사용자가 요청하면 예외를 던진다.")
    @Test
    void deleteReservationThrowByMemberDoesNotMatch(){
        //given
        Member manager = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@naver.com", "!test1234", "test3", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@naver.com", "!test1234", "test4", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member5 = Member.create("test5@naver.com", "!test1234", "test5", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member6 = Member.create("test6@naver.com", "!test1234", "test6", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member7 = Member.create("test7@naver.com", "!test1234", "test7", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        Member member8 = Member.create("test8@naver.com", "!test1234", "test8", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member merchant = Member.create("test2@naver.com", "!test1234", "test2", "010-2234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.MERCHANT, TermsAgreed.AGREE);
        Stadium stadium = Stadium.create(merchant, "스타 구장", "인천 송도", "010-2234-1234", "깔끔 쾌적", 36, 36);
        Team team = Team.create(stadium.getStadiumId(), "한화", "이길 수 있을까", 5, 10, 10, "인천");
        Team team2 = Team.create(stadium.getStadiumId(), "삼성", "이길 수 있을까", 10, 10, 5, "인천");

        Court court = Court.create(stadium, "A번 구장", "잔디 구장", BigDecimal.valueOf(5000));

        Reservation reservation = Reservation.create(court, manager, team, LocalDateTime.of(2024, 10, 20, 12,00,00), ReservationStatus.RECRUITING, Gender.MALE);
        Reservation reservation2 = Reservation.create(court, manager, team2, LocalDateTime.of(2024, 10, 20, 12,00,00), ReservationStatus.RECRUITING, Gender.MALE);


        Participant participant1 = Participant.create(reservation, manager, ParticipantRole.MEMBER);
        Participant participant2 = Participant.create(reservation, member3, ParticipantRole.MEMBER);
        Participant participant3 = Participant.create(reservation, member4, ParticipantRole.MEMBER);
        Participant participant4 = Participant.create(reservation, member5, ParticipantRole.MEMBER);
        Participant participant5 = Participant.create(reservation, member6, ParticipantRole.MEMBER);
        Participant participant6 = Participant.create(reservation, member7, ParticipantRole.MEMBER);

        Mercenary mercenary = Mercenary.create(reservation,"용병 구합니다");

        Game game = Game.create(reservation, reservation2, GameStatus.READY);

        memberRepository.saveAll(List.of(manager, merchant, member3, member4, member5, member6, member7, member8));
        stadiumRepository.save(stadium);
        teamRepository.saveAll(List.of(team, team2));
        courtRepository.save(court);
        reservationRepository.saveAll(List.of(reservation, reservation2));
        participantRepository.saveAll(List.of(participant1, participant2, participant3, participant4, participant5, participant6));
        mercenaryRepository.save(mercenary);
        gameRepository.save(game);


        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> jkReservationService.deleteReservation(reservation.getReservationId(), member7),
                "일치하지 않는 사용자가 요청할 경우 IllegalArgumentException이 발생해야 합니다.");
    }

    @DisplayName("예약을 삭제할 때 이미 Confirm 된 예약을 취소할 때 예외를 던진다.")
    @Test
    void deleteReservationThrowByAlreadyConfirmedReservation(){
        //given
        Member manager = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member3 = Member.create("test3@naver.com", "!test1234", "test3", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member4 = Member.create("test4@naver.com", "!test1234", "test4", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member5 = Member.create("test5@naver.com", "!test1234", "test5", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member6 = Member.create("test6@naver.com", "!test1234", "test6", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member member7 = Member.create("test7@naver.com", "!test1234", "test7", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        Member member8 = Member.create("test8@naver.com", "!test1234", "test8", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member merchant = Member.create("test2@naver.com", "!test1234", "test2", "010-2234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.MERCHANT, TermsAgreed.AGREE);
        Stadium stadium = Stadium.create(merchant, "스타 구장", "인천 송도", "010-2234-1234", "깔끔 쾌적", 36, 36);
        Team team = Team.create(stadium.getStadiumId(), "한화", "이길 수 있을까", 5, 10, 10, "인천");
        Team team2 = Team.create(stadium.getStadiumId(), "삼성", "이길 수 있을까", 10, 10, 5, "인천");

        Court court = Court.create(stadium, "A번 구장", "잔디 구장", BigDecimal.valueOf(5000));

        Reservation reservation = Reservation.create(court, manager, team, LocalDateTime.of(2024, 10, 20, 12,00,00), ReservationStatus.CONFIRMED, Gender.MALE);
        Reservation reservation2 = Reservation.create(court, manager, team2, LocalDateTime.of(2024, 10, 20, 12,00,00), ReservationStatus.RECRUITING, Gender.MALE);


        Participant participant1 = Participant.create(reservation, manager, ParticipantRole.MEMBER);
        Participant participant2 = Participant.create(reservation, member3, ParticipantRole.MEMBER);
        Participant participant3 = Participant.create(reservation, member4, ParticipantRole.MEMBER);
        Participant participant4 = Participant.create(reservation, member5, ParticipantRole.MEMBER);
        Participant participant5 = Participant.create(reservation, member6, ParticipantRole.MEMBER);
        Participant participant6 = Participant.create(reservation, member7, ParticipantRole.MEMBER);

        Mercenary mercenary = Mercenary.create(reservation,"용병 구합니다");


        Game game = Game.create(reservation, reservation2, GameStatus.READY);

        memberRepository.saveAll(List.of(manager, merchant, member3, member4, member5, member6, member7, member8));
        stadiumRepository.save(stadium);
        teamRepository.saveAll(List.of(team, team2));
        courtRepository.save(court);
        reservationRepository.saveAll(List.of(reservation, reservation2));
        participantRepository.saveAll(List.of(participant1, participant2, participant3, participant4, participant5, participant6));
        mercenaryRepository.save(mercenary);
        gameRepository.save(game);


        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> jkReservationService.deleteReservation(reservation.getReservationId(), manager),
                "일치하지 않는 사용자가 요청할 경우 IllegalArgumentException이 발생해야 합니다.");
    }

}