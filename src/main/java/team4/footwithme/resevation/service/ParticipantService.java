package team4.footwithme.resevation.service;

import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.service.request.ParticipantUpdateServiceRequest;
import team4.footwithme.resevation.service.response.ParticipantResponse;

import java.util.List;

public interface ParticipantService {
    ParticipantResponse createMercenaryParticipant(Long mercenaryId, Member member);

    ParticipantResponse createParticipant(Long reservationId, Member member);

    Long deleteParticipant(Long reservationId, Member member);

    ParticipantResponse updateMercenaryParticipant(ParticipantUpdateServiceRequest request, Member member);

    List<ParticipantResponse> getAcceptParticipants(Long reservationId);

    List<ParticipantResponse> getParticipants(Long reservationId);

    List<ParticipantResponse> getParticipantsMercenary(Long reservationId);
}
