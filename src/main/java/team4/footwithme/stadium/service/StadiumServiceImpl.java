package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    private final MemberRepository memberRepository;

    // 구장 목록 조회
    @Override
    public Slice<StadiumsResponse> getStadiumList(Pageable pageable) {
        return stadiumRepository.findAllActiveStadiums(pageable).map(StadiumsResponse::from);
    }

    // 구장 상세 정보 조회
    @Override
    public StadiumDetailResponse getStadiumDetail(Long id) {
        return StadiumDetailResponse.from((Stadium) findEntityByIdOrThrowException(stadiumRepository, id, ExceptionMessage.STADIUM_NOT_FOUND));
    }

    // 이름으로 구장 검색
    @Override
    public Slice<StadiumsResponse> getStadiumsByName(String query, Pageable pageable) {
        return stadiumRepository.findByNameContainingIgnoreCase(query, pageable).map(StadiumsResponse::from);
    }

    // 주소로 구장 검색
    @Override
    public Slice<StadiumsResponse> getStadiumsByAddress(String address, Pageable pageable) {
        return stadiumRepository.findByAddressContainingIgnoreCase(address, pageable).map(StadiumsResponse::from);
    }

    // 위도, 경도의 일정 거리 내의 구장 목록 반환
    @Override
    public Slice<StadiumsResponse> getStadiumsWithinDistance(StadiumSearchByLocationServiceRequest request, Pageable pageable) {
        return stadiumRepository.findStadiumsByLocation(request.latitude(), request.longitude(), request.distance(), pageable)
                .map(StadiumsResponse::from);
    }

    // 풋살장 등록
    @Override
    @Transactional
    public StadiumDetailResponse registerStadium(StadiumRegisterServiceRequest request, Long memberId) {
        Member member = (Member) findEntityByIdOrThrowException(memberRepository, memberId, ExceptionMessage.MEMBER_NOT_FOUND);
        Stadium stadium = Stadium.create(member, request.name(), request.address(), request.phoneNumber(), request.description(),
                request.latitude(), request.longitude());

        stadiumRepository.save(stadium);
        return StadiumDetailResponse.from(stadium);
    }

    // 풋살장 정보 수정
    @Override
    @Transactional
    public StadiumDetailResponse updateStadium(StadiumUpdateServiceRequest request, Long memberId, Long stadiumId) {
        Stadium stadium = validateStadiumOwnership(memberId, stadiumId);
        stadium.updateStadium(request.name(), request.address(), request.phoneNumber(), request.description(),
                request.latitude(), request.longitude());
        return StadiumDetailResponse.from(stadium);
    }

    // 풋살장 삭제
    @Override
    @Transactional
    public void deleteStadium(Long memberId, Long stadiumId) {
        Stadium stadium = validateStadiumOwnership(memberId, stadiumId);
        stadiumRepository.delete(stadium);
    }

    private Stadium validateStadiumOwnership(Long memberId, Long stadiumId) {
        findEntityByIdOrThrowException(memberRepository, memberId, ExceptionMessage.MEMBER_NOT_FOUND);
        Stadium stadium = (Stadium) findEntityByIdOrThrowException(stadiumRepository, stadiumId, ExceptionMessage.STADIUM_NOT_FOUND);
        if (!stadium.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }
        return stadium;
    }

    private <T> T findEntityByIdOrThrowException(CustomGlobalRepository<T> repository, Long id, ExceptionMessage exceptionMessage) {
        return repository.findActiveById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, exceptionMessage);
                    return new IllegalArgumentException(exceptionMessage.getText());
                });
    }
}
