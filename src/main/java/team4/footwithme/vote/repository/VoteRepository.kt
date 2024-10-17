package team4.footwithme.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.vote.domain.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>, CustomVoteRepository {
}
