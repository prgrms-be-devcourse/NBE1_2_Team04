package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest;
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest;
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    private final StadiumRepository stadiumRepository;

    private final MemberRepository memberRepository;

    public List<CourtsResponse> getCourtsByStadiumId(Long stadiumId) {
        findStadiumByIdOrThrowException(stadiumId);
        List<Court> courts = courtRepository.findByStadium_StadiumId(stadiumId);
        return Optional.ofNullable(courts)
                .orElse(Collections.emptyList())
                .stream()
                .map(CourtsResponse::from)
                .collect(Collectors.toList());
    }

    public List<CourtsResponse> getAllCourts() {
        List<Court> courts = courtRepository.findAllActive();
        return Optional.of(courts)
                .orElse(Collections.emptyList())
                .stream()
                .map(CourtsResponse::from)
                .collect(Collectors.toList());
    }

    public CourtDetailResponse getCourtByCourtId(Long courtId) {
        Court court = findCourtByIdOrThrowException(courtId);
        return CourtDetailResponse.from(court);
    }

    // TODO : 중복 코드가 좀 많아서 나중에 리펙토링 할 것
    @Transactional
    public CourtDetailResponse registerCourt(CourtRegisterServiceRequest request, Long memberId) {
        findMemberByIdOrThrowException(memberId);
        Stadium stadium = findStadiumByIdOrThrowException(request.stadiumId());
        if (!stadium.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }

        Court court = Court.create(
                stadium,
                request.name(),
                request.description(),
                request.price_per_hour()
        );

        courtRepository.save(court);

        return CourtDetailResponse.from(court);
    }

    @Transactional
    public CourtDetailResponse updateCourt(CourtUpdateServiceRequest request, Long memberId, Long courtId) {
        findMemberByIdOrThrowException(memberId);
        Stadium stadium = findStadiumByIdOrThrowException(request.stadiumId());
        if (!stadium.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }
        Court court = findCourtByIdOrThrowException(courtId);
        court.updateCourt(request.name(), request.description(), request.price_per_hour());
        return CourtDetailResponse.from(court);
    }

    @Transactional
    public void deleteCourt(CourtDeleteServiceRequest request, Long memberId, Long courtId) {
        System.out.println(memberId);
        findMemberByIdOrThrowException(memberId);
        Stadium stadium = findStadiumByIdOrThrowException(request.StadiumId());
        if (!stadium.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }
        Court court = findCourtByIdOrThrowException(courtId);
        courtRepository.delete(court);
    }

    // 구장 조회 예외처리
    public Court findCourtByIdOrThrowException(long id) {
        return courtRepository.findByCourtId(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, ExceptionMessage.COURT_NOT_FOUND);
                    return new IllegalArgumentException(ExceptionMessage.COURT_NOT_FOUND.getText());
                });
    }

    // 풋살장 조회 예외처리
    public Stadium findStadiumByIdOrThrowException(long id) {
        return stadiumRepository.findByStadiumId(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, ExceptionMessage.STADIUM_NOT_FOUND);
                    return new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_FOUND.getText());
                });
    }

    //맴버 조회 예외처리
    public Member findMemberByIdOrThrowException(long id) {
        return memberRepository.findByMemberId(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, ExceptionMessage.MEMBER_NOT_FOUND);
                    return new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_FOUND.getText());
                });
    }
}
