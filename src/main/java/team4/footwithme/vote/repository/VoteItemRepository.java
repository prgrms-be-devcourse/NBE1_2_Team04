package team4.footwithme.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.vote.domain.VoteItem;

import java.util.List;

@Repository
public interface VoteItemRepository extends JpaRepository<VoteItem, Long>, CustomVoteItemRepository {

    List<VoteItem> findByVoteVoteId(Long voteId);
}
