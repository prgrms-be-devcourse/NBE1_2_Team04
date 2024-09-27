package team4.footwithme.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.member.domain.Member;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findByEmail(String email);
}
