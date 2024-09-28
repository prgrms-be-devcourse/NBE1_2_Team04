package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.vote.domain.VoteItem;
import team4.footwithme.vote.domain.VoteItemDate;
import team4.footwithme.vote.domain.VoteItemLocate;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.repository.ChoiceRepository;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteStadiumCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final MemberRepository memberRepository;
    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;
    private final ChoiceRepository choiceRepository;

    @Transactional
    @Override
    public VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberId(email);
        validateTeamId(teamId);
        List<Long> stadiumIds = request.stadiumIds();
        validateStadiumIds(stadiumIds);

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);
        List<VoteItem> voteItems = savedVoteItems.stream().map(voteItem -> (VoteItem) voteItem).toList();

        List<String> stadiumNames = getStadiumNames(voteItems);

        List<VoteItemResponse> voteItemResponse = getVoteItemLocateResponse(voteItems, stadiumNames);
        return VoteResponse.of(vote, voteItemResponse);
    }

    private void validateTeamId(Long teamId) {
        teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    private Long getMemberId(String email) {
        Long memberId = memberRepository.findMemberIdByMemberEmail(email);
        if (memberId == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return memberId;
    }

    private void validateStadiumIds(List<Long> requestStadiumIds) {
        if (stadiumRepository.countStadiumByStadiumIds(requestStadiumIds) != requestStadiumIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 구장이 포함되어 있습니다.");
        }
    }

    private List<String> getStadiumNames(List<VoteItem> savedVoteItems) {
        List<Long> stadiumIds = savedVoteItems.stream()
            .filter(voteItem -> voteItem instanceof VoteItemLocate)
            .map(voteItem -> (VoteItemLocate) voteItem)
            .map(VoteItemLocate::getStadiumId)
            .toList();

        return stadiumRepository.findStadiumNamesByStadiumIds(stadiumIds);
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getStadiumVote(Long voteId) {
        Vote vote = getVote(voteId);
        List<VoteItem> voteItems = voteItemRepository.findByVoteVoteId(voteId);
        List<String> stadiumNames = getStadiumNames(voteItems);
        List<VoteItemResponse> voteItemResponse = getVoteItemLocateResponse(voteItems, stadiumNames);
        return VoteResponse.of(vote, voteItemResponse);
    }

    private List<VoteItemResponse> getVoteItemLocateResponse(List<VoteItem> voteItems, List<String> stadiumNames) {
        return voteItems.stream()
            .map(voteItem -> createVoteItemLocateResponse(voteItems, stadiumNames, voteItem))
            .toList();
    }

    private VoteItemResponse createVoteItemLocateResponse(List<VoteItem> voteItems, List<String> stadiumNames, VoteItem voteItem) {
        Long voteItemId = voteItem.getVoteItemId();
        Long voteCount = choiceRepository.countByVoteItemId(voteItemId);
        String contents = stadiumNames.get(voteItems.indexOf(voteItem));
        return VoteItemResponse.of(voteItemId, contents, voteCount);
    }

    private Vote getVote(Long voteId) {
        return voteRepository.findById(voteId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 투표입니다."));
    }

    @Transactional
    @Override
    public VoteResponse createDateVote(VoteDateCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberId(email);
        validateTeamId(teamId);

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
        Vote savedVote = voteRepository.save(vote);

        List<LocalDateTime> choices = request.choices().stream()
            .toList();

        List<VoteItemDate> voteItems = voteItemRepository.saveAll(choices.stream()
            .map(choice -> VoteItemDate.create(savedVote, choice))
            .toList());

        List<VoteItemResponse> voteItemResponse = getVoteItemDateResponse(voteItems);
        return VoteResponse.of(savedVote, voteItemResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getDateVote(Long voteId) {
        Vote vote = getVote(voteId);
        voteItemRepository.findByVoteVoteId(voteId);

        List<VoteItemDate> voteItems = voteItemRepository.findByVoteVoteId(voteId).stream()
            .map(voteItem -> (VoteItemDate) voteItem)
            .toList();

        List<VoteItemResponse> voteItemResponse = getVoteItemDateResponse(voteItems);
        return VoteResponse.of(vote, voteItemResponse);
    }

    private List<VoteItemResponse> getVoteItemDateResponse(List<VoteItemDate> voteItems) {
        return voteItems.stream()
            .map(this::mapToVoteItemResponse)
            .toList();
    }

    private VoteItemResponse mapToVoteItemResponse(VoteItemDate voteItem) {
        Long voteItemId = voteItem.getVoteItemId();
        String content = voteItem.getTime().toString();
        Long count = choiceRepository.countByVoteItemId(voteItemId);
        return VoteItemResponse.of(voteItemId, content, count);
    }

}
