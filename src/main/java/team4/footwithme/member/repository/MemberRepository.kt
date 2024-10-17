package team4.footwithme.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.member.domain.Member
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member?, Long?>, CustomMemberRepository, CustomGlobalRepository<Any?> {
    @Query("select m from Member m where m.isDeleted = 'false' and m.memberId = :id")
    fun findByMemberId(@Param("id") memberId: Long?): Optional<Member?>?

    @Query("select m from Member m where m.isDeleted = 'false' and m.email = :email")
    fun findByEmail(email: String?): Optional<Member?>

    @Query("select m from Member m where m.isDeleted = 'false' and m.memberId = :id")
    fun findActiveById(@Param("id") memberId: Long): Optional<Any?>?
}