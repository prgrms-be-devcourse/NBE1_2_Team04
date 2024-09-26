package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.vote.domain.VoteItemLocate;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;
import team4.footwithme.vote.service.request.VoteStadiumCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final VoteItemRepository voteItemRepository;

    private final MemberRepository memberRepository;

    private final StadiumRepository stadiumRepository;

    private final TeamRepository teamRepository;

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

        return VoteResponse.of(savedVote, savedVoteItems.stream()
            .map(voteItem -> VoteItemResponse.of(voteItem.getVoteItemId(), stadiumNames.get(savedVoteItems.indexOf(voteItem)), 0L))
            .toList()
        );
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
        if (stadiumRepository.findAllById(requestStadiumIds).size() != requestStadiumIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 구장이 포함되어 있습니다.");
        }
    }

    private List<String> getStadiumNames(List<VoteItemLocate> savedVoteItems) {
        return stadiumRepository.findStadiumNamesByStadiumIds(savedVoteItems.stream()
            .map(VoteItemLocate::getStadiumId)
            .toList()
        );
    }
}
