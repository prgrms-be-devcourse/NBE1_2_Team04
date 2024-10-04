package team4.footwithme.vote.repository;

import team4.footwithme.vote.domain.Vote;

import java.util.Optional;

public interface CustomVoteRepository {

    Optional<Vote> findNotDeletedVoteById(Long id);

}
