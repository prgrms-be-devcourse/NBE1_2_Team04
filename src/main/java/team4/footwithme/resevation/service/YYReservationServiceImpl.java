package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.resevation.domain.*;
import team4.footwithme.resevation.repository.MWParticipantRepository;
import team4.footwithme.resevation.repository.YYGameRepository;
import team4.footwithme.resevation.repository.YYReservationRepository;
import team4.footwithme.resevation.service.response.YYReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.YYReservationInfoResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class YYReservationServiceImpl implements YYReservationService{

    private final YYReservationRepository reservationRepository;
    private final MWParticipantRepository participantRepository;
    private final YYGameRepository gameRepository;

    // TODO :: 페이지 네이션
    @Override
    @Transactional(readOnly = true)
    public List<YYReservationInfoResponse> getTeamReservationInfo(Long teamId) {

        List<Reservation> reservations = findByTeamTeamIdOrThrowException(teamId);

        List<YYReservationInfoResponse> list = reservations.stream()
                .map(YYReservationInfoResponse::from)
                .collect(Collectors.toList());

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public YYReservationInfoDetailsResponse getTeamReservationInfoDetails(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        List<Participant> participants = participantRepository.findParticipantsByReservationId(reservationId);

        //상대팀 조회
        Reservation matchedTeam = gameRepository.findFirstTeamReservationBySecondTeamReservationId(reservationId)
                .orElse(null);
        //상대팀 이름 --> 없으면 null
        String matchTeamName;
        if(matchedTeam == null) {
            matchTeamName = null;
        } else{
            matchTeamName = matchedTeam.getTeam().getName();
        }

        return YYReservationInfoDetailsResponse.of(reservation, participants, matchTeamName);
    }

    public List<Reservation> findByTeamTeamIdOrThrowException(Long teamId) {
        List<Reservation> result = reservationRepository.findByTeamTeamId(teamId);
        if(result.isEmpty()){
            throw new IllegalArgumentException("해당 팀이 존재하지 않습니다.");
        }
        return result;
    }
}
