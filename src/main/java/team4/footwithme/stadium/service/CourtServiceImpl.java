package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
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
import team4.footwithme.stadium.util.SortFieldMapper;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    private final StadiumRepository stadiumRepository;

    private final MemberRepository memberRepository;

    @Override
    public Slice<CourtsResponse> getCourtsByStadiumId(Long stadiumId, Integer page, String sort) {
        findEntityByIdOrThrowException(stadiumRepository, stadiumId, ExceptionMessage.STADIUM_NOT_FOUND);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)));
        Slice<Court> courts = courtRepository.findByStadium_StadiumId(stadiumId, pageable);
        return courts.map(CourtsResponse::from);
    }

    @Override
    public Slice<CourtsResponse> getAllCourts(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)));
        Slice<Court> courts = courtRepository.findAllActive(pageable);
        return courts.map(CourtsResponse::from);
    }

    @Override
    public CourtDetailResponse getCourtByCourtId(Long courtId) {
        Court court = (Court) findEntityByIdOrThrowException(courtRepository, courtId, ExceptionMessage.COURT_NOT_FOUND);
        return CourtDetailResponse.from(court);
    }

    @Override
    @Transactional
    public CourtDetailResponse registerCourt(CourtRegisterServiceRequest request, Long memberId) {
        validateStadiumOwnership(request.stadiumId(), memberId).createCourt(memberId);
        Court court = Court.create(
                (Stadium) findEntityByIdOrThrowException(stadiumRepository, request.stadiumId(), ExceptionMessage.STADIUM_NOT_FOUND),
                request.name(),
                request.description(),
                request.price_per_hour()
        );
        courtRepository.save(court);
        return CourtDetailResponse.from(court);
    }

    @Override
    @Transactional
    public CourtDetailResponse updateCourt(CourtUpdateServiceRequest request, Long memberId, Long courtId) {
        validateStadiumOwnership(request.stadiumId(), memberId);
        Court court = (Court) findEntityByIdOrThrowException(courtRepository, courtId, ExceptionMessage.COURT_NOT_FOUND);
        court.updateCourt(request.stadiumId(), memberId, request.name(), request.description(), request.price_per_hour());
        return CourtDetailResponse.from(court);
    }

    @Override
    @Transactional
    public void deleteCourt(CourtDeleteServiceRequest request, Long memberId, Long courtId) {
        validateStadiumOwnership(request.stadiumId(), memberId);
        Court court = (Court) findEntityByIdOrThrowException(courtRepository, courtId, ExceptionMessage.COURT_NOT_FOUND);
        court.deleteCourt(request.stadiumId(), memberId);
        courtRepository.delete(court);
    }

    private Stadium validateStadiumOwnership(Long stadiumId, Long memberId) {
        findEntityByIdOrThrowException(memberRepository, memberId, ExceptionMessage.MEMBER_NOT_FOUND);
        return (Stadium) findEntityByIdOrThrowException(stadiumRepository, stadiumId, ExceptionMessage.STADIUM_NOT_FOUND);
    }

    private <T> T findEntityByIdOrThrowException(CustomGlobalRepository<T> repository, Long id, ExceptionMessage exceptionMessage) {
        return repository.findActiveById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, exceptionMessage);
                    return new IllegalArgumentException(exceptionMessage.getText());
                });
    }
}
