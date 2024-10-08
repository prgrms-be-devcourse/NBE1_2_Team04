package team4.footwithme.resevation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.resevation.domain.Game;
import team4.footwithme.resevation.domain.GameStatus;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.resevation.repository.GameRepository;
import team4.footwithme.resevation.repository.ReservationRepository;
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest;
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest;
import team4.footwithme.resevation.service.response.GameDetailResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class GameServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private Member testMember1;
    private Member testMember2;
    private Court testCourt;
    private Team testTeam;
    private Reservation reservation1;
    private Reservation reservation2;
    private Game testGame;
    private Stadium testStadium;
    private Team team;

    @BeforeEach
    void setUp() {
        testMember1 = Member.builder()
                .email("test1@example.com")
                .password("password")
                .name("Test User 1")
                .phoneNumber("010-1234-5678")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("test1@example.com")
                        .build())
                .gender(Gender.MALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();

        testMember2 = Member.builder()
                .email("test2@example.com")
                .password("password")
                .name("Test User 2")
                .phoneNumber("010-1234-5678")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("test2@example.com")
                        .build())
                .gender(Gender.MALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();

        memberRepository.save(
                Member.create("teamLeader@gmail.com", "123456", "팀장", "010-1111-1111",
                        LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );


        memberRepository.save(testMember1);
        memberRepository.save(testMember2);

        testStadium = Stadium.create(testMember1, "Test Stadium", "Test Address", "010-1111-1111", "Test Description", 37.5665, 126.9780);
        stadiumRepository.save(testStadium);

        testCourt = Court.create(testStadium, "Court1", "Description1", new BigDecimal("10000"));
        courtRepository.save(testCourt);

        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        team = teamRepository.save(Team.create(null, "팀명", "팀 설명", 0, 0, 0, "선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leader));

        reservation1 = Reservation.builder()
                .member(testMember1)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.READY)
                .gender(Gender.MALE)
                .team(team)
                .build();

        reservation2 = Reservation.builder()
                .member(testMember2)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.READY)
                .gender(Gender.MALE)
                .team(team)
                .build();

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);


    }

    @Test
    @DisplayName("성공적으로 게임을 등록해야 한다")
    void registerGame_success() {
        GameRegisterServiceRequest request = new GameRegisterServiceRequest(reservation1.getReservationId(), reservation2.getReservationId());

        GameDetailResponse response = gameService.registerGame(testMember1, request);

        assertThat(response).isNotNull();
        assertThat(response.gameStatus()).isEqualTo(GameStatus.PENDING);

        Optional<Game> savedGame = gameRepository.findById(response.gameId());
        assertThat(savedGame).isPresent();
        savedGame.ifPresent(game -> {
            assertThat(game.getFirstTeamReservation().getReservationId()).isEqualTo(reservation1.getReservationId());
            assertThat(game.getSecondTeamReservation().getReservationId()).isEqualTo(reservation2.getReservationId());
            assertThat(game.getGameStatus()).isEqualTo(GameStatus.PENDING);
        });
    }

    @Test
    @DisplayName("예약한 회원이 일치하지 않으면 예외를 발생시켜야 한다")
    void registerGame_memberMismatch_throwsException() {
        Member anotherMember = Member.builder()
                .email("another@example.com")
                .password("password")
                .name("Another User")
                .phoneNumber("010-5678-1234")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("another@example.com")
                        .build())
                .gender(Gender.FEMALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();
        memberRepository.save(anotherMember);

        GameRegisterServiceRequest request = new GameRegisterServiceRequest(reservation1.getReservationId(), reservation2.getReservationId());

        assertThatThrownBy(() -> gameService.registerGame(anotherMember, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.RESERVATION_MEMBER_NOT_MATCH.getText());
    }

    @Test
    @DisplayName("게임 상태를 성공적으로 업데이트해야 한다. Ready")
    void updateGameStatus_success() {
        testGame = Game.builder()
                .firstTeamReservation(reservation1)
                .secondTeamReservation(reservation2)
                .gameStatus(GameStatus.PENDING)
                .build();

        gameRepository.save(testGame);

        GameStatusUpdateServiceRequest request = new GameStatusUpdateServiceRequest(testGame.getGameId(), GameStatus.READY);

        String result = gameService.updateGameStatus(testMember2, request);

        assertThat(result).isEqualTo(ExceptionMessage.RESERVATION_SUCCESS.getText());

        Optional<Game> updatedGame = gameRepository.findById(testGame.getGameId());
        assertThat(updatedGame).isPresent();
        updatedGame.ifPresent(game -> {
            assertThat(game.getGameStatus()).isEqualTo(GameStatus.READY);
        });
    }

    @Test
    @DisplayName("게임 상태를 성공적으로 업데이트해야 한다. Ignore")
    void updateGameStatus_ignore() {
        testGame = Game.builder()
                .firstTeamReservation(reservation1)
                .secondTeamReservation(reservation2)
                .gameStatus(GameStatus.PENDING)
                .build();

        gameRepository.save(testGame);

        GameStatusUpdateServiceRequest request = new GameStatusUpdateServiceRequest(testGame.getGameId(), GameStatus.IGNORE);

        String result = gameService.updateGameStatus(testMember2, request);

        assertThat(result).isEqualTo("해당 매칭을 거절하였습니다.");

        Optional<Game> updatedGame = gameRepository.findById(testGame.getGameId());
        assertThat(updatedGame).isPresent();
        updatedGame.ifPresent(game -> {
            assertThat(game.getGameStatus()).isEqualTo(GameStatus.IGNORE);
        });
    }

    @Test
    @DisplayName("이미 예약이 존재한다면, 예약은 Canceled 되어야 한다.")
    void updateGameStatus_fail() {
        testGame = Game.builder()
                .firstTeamReservation(reservation1)
                .secondTeamReservation(reservation2)
                .gameStatus(GameStatus.PENDING)
                .build();

        gameRepository.save(testGame);

        Reservation reservationOther = Reservation.builder()
                .member(testMember1)
                .matchDate(LocalDateTime.parse("2024-10-01T10:00"))
                .court(testCourt)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .gender(Gender.MALE)
                .team(team)
                .build();

        reservationRepository.save(reservationOther);

        GameStatusUpdateServiceRequest request = new GameStatusUpdateServiceRequest(testGame.getGameId(), GameStatus.READY);

        String result = gameService.updateGameStatus(testMember2, request);

        assertThat(result).isEqualTo(ExceptionMessage.RESERVATION_CONFLICT.getText());

        Optional<Game> updatedGame = gameRepository.findById(testGame.getGameId());
        assertThat(updatedGame).isPresent();
        updatedGame.ifPresent(game -> {
            assertThat(game.getGameStatus()).isEqualTo(GameStatus.IGNORE);
        });

        Optional<Reservation> updatedReservation = reservationRepository.findById(reservation1.getReservationId());
        assertThat(updatedReservation).isPresent();
        assertThat(updatedReservation.get().getReservationStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }


    @Test
    @DisplayName("대기 중인 게임을 성공적으로 조회해야 한다")
    void findPendingGames_success() {
        testGame = Game.builder()
                .firstTeamReservation(reservation1)
                .secondTeamReservation(reservation2)
                .gameStatus(GameStatus.PENDING)
                .build();
        gameRepository.save(testGame);

        Slice<GameDetailResponse> result = gameService.findPendingGames(testMember2, reservation2.getReservationId(), 0);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).gameStatus()).isEqualTo(GameStatus.PENDING);
    }
}
