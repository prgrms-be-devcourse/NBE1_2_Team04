package team4.footwithme.vote.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.domain.Member;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

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
    private final ApplicationEventPublisher eventPublisher;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @PostConstruct
    private void initializeScheduledTasks() {
        List<Vote> activeVotes = voteRepository.findOpenedVotes();
        for (Vote vote : activeVotes) {
            addVoteTaskToTaskSchedule(vote);
        }
    }

    @Transactional
    @Override
    public VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberIdByEmail(email);
        validateTeamByTeamId(teamId);

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
        Vote savedVote = voteRepository.save(vote);
        addVoteTaskToTaskSchedule(savedVote);

        List<VoteItemLocate> voteItemLocates = createVoteItemLocate(request, savedVote);

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems);
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getStadiumVote(Long voteId) {
        Vote vote = getVoteByVoteId(voteId);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems());
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse createDateVote(VoteDateCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberIdByEmail(email);
        validateTeamByTeamId(teamId);

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
        Vote savedVote = voteRepository.save(vote);
        addVoteTaskToTaskSchedule(savedVote);

        List<VoteItemDate> savedVoteItems = createVoteItemDate(request, savedVote);

        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems);
        return VoteResponse.of(savedVote, voteItemResponses);
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getDateVote(Long voteId) {
        Vote vote = getVoteByVoteId(voteId);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems());
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public Long deleteVote(Long voteId, String email) {
        Vote vote = getVoteByVoteId(voteId);
        vote.checkWriterFromMemberId(getMemberIdByEmail(email));
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

        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems());

        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse deleteChoice(Long voteId, String email) {
        Long memberId = getMemberIdByEmail(email);
        Vote vote = getVoteByVoteId(voteId);

        List<Choice> choices = choiceRepository.findByMemberIdAndVoteId(memberId, voteId);

        choiceRepository.deleteAllInBatch(choices);
        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems());
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse updateVote(VoteUpdateServiceRequest serviceRequest, Long voteId, String email) {
        Long memberId = getMemberIdByEmail(email);
        Vote vote = getVoteByVoteId(voteId);

        vote.update(serviceRequest.title(), serviceRequest.endAt(), memberId);

        List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems());
        return VoteResponse.of(vote, voteItemResponses);
    }

    @Transactional
    @Override
    public VoteResponse closeVote(Long voteId, Member member) {
        Vote vote = voteRepository.findNotDeletedVoteById(voteId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 투표가 없습니다."));
        vote.checkWriterFromMemberId(member.getMemberId());
        vote.updateVoteStatusToClose();
        cancelTaskInSchedulerFromVoteId(voteId);
        return VoteResponse.of(vote, convertVoteItemsToResponseFrom(vote.getVoteItems()));
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
        return voteRepository.findNotDeletedVoteById(voteId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 투표입니다."));
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
            choiceRepository.findMemberIdsByVoteItemId(voteItemDate.getVoteItemId())
        );
    }

    private VoteItemResponse convertToVoteItemResponseFrom(VoteItemLocate voteItemLocate) {
        return VoteItemResponse.of(
            voteItemLocate.getVoteItemId(),
            stadiumRepository.findStadiumNameById(voteItemLocate.getStadiumId()),
            choiceRepository.findMemberIdsByVoteItemId(voteItemLocate.getVoteItemId())
        );
    }

    private void addVoteTaskToTaskSchedule(Vote vote) {
        taskScheduler.schedule(publishClosedVoteTask(vote.getVoteId()), vote.getInstantEndAt());
    }

    private Runnable publishClosedVoteTask(Long voteId) {
        return () -> eventPublisher.publishEvent(new RegisteredVoteEvent(voteId));
    }

    private void cancelTaskInSchedulerFromVoteId(Long voteId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(voteId);
        if (scheduledTask == null) {
            throw new IllegalArgumentException("해당하는 투표 ID로 등록된 작업이 없습니다.");
        }
        scheduledTask.cancel(false);
        scheduledTasks.remove(voteId);
    }

}
