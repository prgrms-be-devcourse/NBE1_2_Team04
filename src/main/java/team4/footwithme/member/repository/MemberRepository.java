package team4.footwithme.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team4.footwithme.member.domain.Member;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    @Query("select m from Member m where m.isDeleted = 'false' and m.email = :email")
    Optional<Member> findByEmail(String email);
}
