package team4.footwithme.stadium.service;

import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;
import team4.footwithme.stadium.util.SortFieldMapper;

import java.util.List;

@Service
public class StadiumServiceImpl implements StadiumService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(StadiumServiceImpl.class);
    private final StadiumRepository stadiumRepository;

    private final MemberRepository memberRepository;

    private final CourtRepository courtRepository;

    public StadiumServiceImpl(StadiumRepository stadiumRepository, MemberRepository memberRepository, CourtRepository courtRepository) {
        this.stadiumRepository = stadiumRepository;
        this.memberRepository = memberRepository;
        this.courtRepository = courtRepository;
    }

    @Override
    public Slice<StadiumsResponse> getStadiumList(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)));
        return stadiumRepository.findAllActiveStadiums(pageable).map(StadiumsResponse::from);
    }

    @Override
    public StadiumDetailResponse getStadiumDetail(Long id) {
        return StadiumDetailResponse.from((Stadium) findEntityByIdOrThrowException(stadiumRepository, id, ExceptionMessage.STADIUM_NOT_FOUND));
    }

    @Override
    public Slice<StadiumsResponse> getStadiumsByName(String query, Integer page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)));
        return stadiumRepository.findByNameContainingIgnoreCase(query, pageable).map(StadiumsResponse::from);
    }

    @Override
    public Slice<StadiumsResponse> getStadiumsByAddress(String address, Integer page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)));
        return stadiumRepository.findByAddressContainingIgnoreCase(address, pageable).map(StadiumsResponse::from);
    }

    @Override
    public Slice<StadiumsResponse> getStadiumsWithinDistance(StadiumSearchByLocationServiceRequest request, Integer page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)));
        return stadiumRepository.findStadiumsByLocation(request.latitude(), request.longitude(), request.distance(), pageable)
            .map(StadiumsResponse::from);
    }

    @Override
    @Transactional
    public StadiumDetailResponse registerStadium(StadiumRegisterServiceRequest request, Member member) {
        Stadium stadium = Stadium.create(member, request.name(), request.address(), request.phoneNumber(), request.description(),
            request.latitude(), request.longitude());

        stadiumRepository.save(stadium);
        return StadiumDetailResponse.from(stadium);
    }

    @Override
    @Transactional
    public StadiumDetailResponse updateStadium(StadiumUpdateServiceRequest request, Member member, Long stadiumId) {
        Stadium stadium = validateStadiumOwnership(member.getMemberId(), stadiumId);
        stadium.updateStadium(member.getMemberId(), request.name(), request.address(), request.phoneNumber(), request.description(),
            request.latitude(), request.longitude());
        return StadiumDetailResponse.from(stadium);
    }

    @Override
    @Transactional
    public void deleteStadium(Member member, Long stadiumId) {
        Stadium stadium = validateStadiumOwnership(member.getMemberId(), stadiumId);
        stadium.deleteStadium(member.getMemberId());
        List<Court> courts = courtRepository.findActiveByStadiumId(stadiumId);
        if (!courts.isEmpty()) courtRepository.deleteAll(courts);
        stadiumRepository.delete(stadium);
    }

    private Stadium validateStadiumOwnership(Long memberId, Long stadiumId) {
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
