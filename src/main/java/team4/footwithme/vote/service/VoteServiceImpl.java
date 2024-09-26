package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.vote.domain.VoteItemLocate;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;
import team4.footwithme.vote.service.request.VoteCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteItemResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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
    public VoteResponse createStadiumVote(VoteCreateServiceRequest request, Long teamId, String email) {
        Long memberId = getMemberId(email);
        teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());

        List<Stadium> stadiumIds = stadiumRepository.findAllById(request.choices());
        if (stadiumIds.size() != request.choices().size()) {
            throw new IllegalArgumentException("존재하지 않는 구장이 포함되어 있습니다.");
        }

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = request.choices().stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        List<String> stadiumNames = stadiumRepository.findStadiumNamesByStadiumIds(savedVoteItems.stream()
            .map(VoteItemLocate::getStadiumId)
            .toList()
        );

        return VoteResponse.of(savedVote, savedVoteItems.stream()
            .map(voteItem -> VoteItemResponse.of(voteItem.getVoteItemId(), stadiumNames.get(savedVoteItems.indexOf(voteItem)), 0L))
            .toList()
        );
    }

    private Long getMemberId(String email) {
        Long memberId = memberRepository.findMemberIdByMemberEmail(email);
        if (memberId == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return memberId;
    }
}
