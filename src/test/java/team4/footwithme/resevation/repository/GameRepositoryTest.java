package team4.footwithme.resevation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.resevation.domain.*;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class GameRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private GameRepository gameRepository;

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
    private TeamMemberRepository teamMemberRepository;

    private Member testMember;
    private Stadium testStadium;
    private Game game1;
    private Game game2;
    private Court testCourt;
    private Team team;
    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;
    private Reservation reservation4;

    @BeforeEach
    void setUp() {
        testMember = Member.create("test@example.com", "password", "Test User", "010-1234-5678",
                LoginProvider.ORIGINAL, "test@example.com", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);


        memberRepository.save(
                Member.create("teamLeader@gmail.com", "123456", "팀장", "010-1111-1111",
                        LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );

        testStadium = Stadium.create(testMember, "Test Stadium", "Seoul", "010-1111-1111", "Test Description", 37.5665, 126.9780);
        stadiumRepository.save(testStadium);

        testCourt = Court.create(testStadium, "Court1", "Description1", new BigDecimal("10000"));
        courtRepository.save(testCourt);

        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        team = teamRepository.save(Team.create(null, "팀명", "팀 설명", 0, 0, 0, "선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leader));

        reservation1 = Reservation.builder()
                .member(testMember)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.MALE)
                .team(team)
                .build();

        reservation2 = Reservation.builder()
                .member(testMember)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.MALE)
                .team(team)
                .build();

        reservation3 = Reservation.builder()
                .member(testMember)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.MALE)
                .team(team)
                .build();
        reservation4 = Reservation.builder()
                .member(testMember)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.READY)
                .gender(ParticipantGender.MALE)
                .team(team)
                .build();


        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);

        game1 = Game.create(reservation1, reservation2, GameStatus.PENDING);
        game2 = Game.create(reservation3, reservation4, GameStatus.READY);

        gameRepository.save(game1);
        gameRepository.save(game2);
    }

    @Test
    @DisplayName("findActiveById는 삭제되지 않은 게임을 조회한다")
    void findActiveById_returnsActiveGame() {
        Optional<Game> foundGame = gameRepository.findActiveById(game1.getGameId());

        assertThat(foundGame).isPresent();
        assertThat(foundGame.get()).isEqualTo(game1);
    }

    @Test
    @DisplayName("findActiveById는 삭제된 게임을 조회하지 않는다")
    void findActiveById_doesNotReturnDeletedGame() {
        gameRepository.delete(game1);

        Optional<Game> foundGame = gameRepository.findActiveById(game1.getGameId());

        assertThat(foundGame).isNotPresent();
    }

    @Test
    @DisplayName("findBySecondReservationAndStatus는 주어진 예약과 상태에 맞는 게임을 조회한다")
    void findBySecondReservationAndStatus_returnsMatchingGames() {
        Slice<Game> games = gameRepository.findBySecondReservationAndStatus(reservation2, GameStatus.PENDING, PageRequest.of(0, 10));

        assertThat(games.getContent())
                .hasSize(1)
                .extracting(Game::getGameStatus)
                .containsExactly(GameStatus.PENDING);
    }

    @Test
    @DisplayName("findBySecondReservationAndStatus는 삭제된 게임을 조회하지 않는다")
    void findBySecondReservationAndStatus_doesNotReturnDeletedGames() {
        gameRepository.delete(game1);

        Slice<Game> games = gameRepository.findBySecondReservationAndStatus(reservation2, GameStatus.PENDING, PageRequest.of(0, 10));

        assertThat(games.getContent()).isEmpty();
    }
}