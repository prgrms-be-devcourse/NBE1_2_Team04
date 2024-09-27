package team4.footwithme.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.member.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository{

    @Query("select m from Member m where m.isDeleted = 'false' and m.memberId = :id")
    Optional<Member> findByMemberId(@Param("id")Long memberId);

    Optional<Member> findByEmail(String email);

}
