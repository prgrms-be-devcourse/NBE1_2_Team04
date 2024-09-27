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

        List<String> stadiumNames = getStadiumNames(savedVoteItems);

        List<VoteItemResponse> voteItemResponse = getVoteItemLocateResponse(savedVoteItems, stadiumNames);
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

    private List<String> getStadiumNames(List<VoteItemLocate> savedVoteItems) {
        return stadiumRepository.findStadiumNamesByStadiumIds(savedVoteItems.stream()
            .map(VoteItemLocate::getStadiumId)
            .toList()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public VoteResponse getStadiumVote(long voteId) {
        Vote vote = getVote(voteId);
        List<VoteItemLocate> voteItems = voteItemRepository.findByVoteVoteId(voteId);
        List<String> stadiumNames = getStadiumNames(voteItems);
        List<VoteItemResponse> voteItemResponse = getVoteItemLocateResponse(voteItems, stadiumNames);
        return VoteResponse.of(vote, voteItemResponse);
    }

    private List<VoteItemResponse> getVoteItemLocateResponse(List<VoteItemLocate> voteItems, List<String> stadiumNames) {
        return voteItems.stream()
            .map(voteItem -> createVoteItemLocateResponse(voteItems, stadiumNames, voteItem))
            .toList();
    }

    private VoteItemResponse createVoteItemLocateResponse(List<VoteItemLocate> voteItems, List<String> stadiumNames, VoteItemLocate voteItem) {
        Long voteItemId = voteItem.getVoteItemId();
        Long voteCount = choiceRepository.countByVoteItemId(voteItemId);
        String contents = stadiumNames.get(voteItems.indexOf(voteItem));
        return VoteItemResponse.of(voteItemId, contents, voteCount);
    }

    private Vote getVote(long voteId) {
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

    private List<VoteItemResponse> getVoteItemDateResponse(List<VoteItemDate> voteItems) {
        return voteItems.stream()
            .map(voteItem -> VoteItemResponse.of(voteItem.getVoteItemId(), voteItem.getTime().toString(), 0L))
            .toList();
    }

}
