package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.vote.domain.*;
import team4.footwithme.vote.repository.ChoiceRepository;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteStadiumCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final MemberRepository memberRepository;
    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;
    private final ChoiceRepository choiceRepository;
    private final TaskScheduler taskScheduler;

    @Transactional
    @Override
    public VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberIdByEmail(email);
        validateTeamByTeamId(teamId);

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
        Vote savedVote = voteRepository.save(vote);
        addClosedVoteTaskToTaskSchedule(savedVote.getVoteId());

        List<VoteItemLocate> voteItemLocates = createVoteItemLocate(request, savedVote);

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems);
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getStadiumVote(Long voteId) {
        Vote vote = getVoteByVoteId(voteId);
        List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse createDateVote(VoteDateCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberIdByEmail(email);
        validateTeamByTeamId(teamId);

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
        Vote savedVote = voteRepository.save(vote);
        addClosedVoteTaskToTaskSchedule(savedVote.getVoteId());

        List<VoteItemDate> savedVoteItems = createVoteItemDate(request, savedVote);

        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems);
        return VoteResponse.of(savedVote, voteItemResponses);
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getDateVote(Long voteId) {
        Vote vote = getVoteByVoteId(voteId);
        List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public Long deleteVote(Long voteId, String email) {
        Vote vote = getVoteByVoteId(voteId);
        vote.delete(getMemberIdByEmail(email));
        voteRepository.delete(vote);
        return voteId;
    }

    @Transactional
    @Override
    public VoteResponse createChoice(ChoiceCreateServiceRequest request, Long voteId, String email) {
        Long memberId = getMemberIdByEmail(email);
        Vote vote = getVoteByVoteId(voteId);

        // TODO : 투표 항목 ID가 존재하는지 검증 이걸 vote를 통해서 쓰면 N+1 문제가 발생 할 것으로 보임 그러면 exist를 쓰는게 좋을까? 아니면 fetch join을 쓰는게 좋을까?
        List<Choice> choices = createChoice(request, memberId);

        choiceRepository.saveAll(choices);
        List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);

        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);

        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse deleteChoice(Long voteId, String email) {
        Long memberId = getMemberIdByEmail(email);
        Vote vote = getVoteByVoteId(voteId);

        List<Choice> choices = choiceRepository.findByMemberIdAndVoteId(memberId, voteId);

        choiceRepository.deleteAllInBatch(choices);
        List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse updateVote(VoteUpdateServiceRequest serviceRequest, Long voteId, String email) {
        Long memberId = getMemberIdByEmail(email);
        Vote vote = getVoteByVoteId(voteId);

        vote.update(serviceRequest.title(), serviceRequest.endAt(), memberId);

        List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
        return VoteResponse.of(vote, voteItemResponses);
    }

    private List<VoteItemDate> createVoteItemDate(VoteDateCreateServiceRequest request, Vote savedVote) {
        return voteItemRepository.saveAll(request.choices().stream()
            .map(choice -> VoteItemDate.create(savedVote, choice))
            .toList());
    }

    private List<VoteItemLocate> createVoteItemLocate(VoteStadiumCreateServiceRequest request, Vote savedVote) {
        checkDuplicateStadiumIds(request.stadiumIds());
        return request.stadiumIds().stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();
    }

    private void checkDuplicateStadiumIds(List<Long> requestStadiumIds) {
        if (stadiumRepository.countStadiumByStadiumIds(requestStadiumIds) != requestStadiumIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 구장이 포함되어 있습니다.");
        }
    }

    private List<Choice> createChoice(ChoiceCreateServiceRequest request, Long memberId) {
        return request.voteItemIds().stream()
            .map(voteItemId -> Choice.create(memberId, voteItemId))
            .toList();
    }

    private Vote getVoteByVoteId(Long voteId) {
        return voteRepository.findById(voteId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 투표입니다."));
    }

    private List<VoteItem> getVoteItemsByVoteId(Long voteId) {
        List<VoteItem> voteItems = voteItemRepository.findByVoteVoteId(voteId);
        if (voteItems.isEmpty()) {
            throw new IllegalArgumentException("투표 항목이 존재하지 않습니다.");
        }
        return voteItems;
    }

    private void validateTeamByTeamId(Long teamId) {
        teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    private Long getMemberIdByEmail(String email) {
        Long memberId = memberRepository.findMemberIdByMemberEmail(email);
        if (memberId == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return memberId;
    }

    private <T extends VoteItem> List<VoteItemResponse> convertVoteItemsToResponseFrom(List<T> voteItems) {
        return voteItems.stream()
            .map(this::convertVoteItemToResponse)
            .toList();
    }

    private <T extends VoteItem> VoteItemResponse convertVoteItemToResponse(T voteItem) {
        if (voteItem instanceof VoteItemLocate voteItemLocate) {
            return convertToVoteItemResponseFrom(voteItemLocate);
        }
        if (voteItem instanceof VoteItemDate voteItemDate) {
            return convertToVoteItemResponseFrom(voteItemDate);
        }
        throw new IllegalArgumentException("지원하지 않는 투표 항목입니다.");
    }

    private VoteItemResponse convertToVoteItemResponseFrom(VoteItemDate voteItemDate) {
        return VoteItemResponse.of(
            voteItemDate.getVoteItemId(),
            voteItemDate.getTime().toString(),
            choiceRepository.countByVoteItemId(voteItemDate.getVoteItemId())
        );
    }

    private VoteItemResponse convertToVoteItemResponseFrom(VoteItemLocate voteItemLocate) {
        return VoteItemResponse.of(
            voteItemLocate.getVoteItemId(),
            stadiumRepository.findStadiumNameById(voteItemLocate.getStadiumId()),
            choiceRepository.countByVoteItemId(voteItemLocate.getVoteItemId())
        );
    }

    // 투표 종료시간이 지나면 투표를 종료하는 작업을 스케줄링합니다.
    private void addClosedVoteTaskToTaskSchedule(Long voteId) {
        Vote vote = getVoteByVoteId(voteId);
        taskScheduler.schedule(() -> updateVoteStatusToClose(vote), vote.getEndAt().atZone(ZoneId.systemDefault()).toInstant());
    }

    private void updateVoteStatusToClose(Vote vote) {
        vote.updateVoteStatusToClose();
        voteRepository.save(vote);
    }

}
