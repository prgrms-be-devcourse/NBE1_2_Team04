package team4.footwithme.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.member.domain.QMember

class CustomMemberRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomMemberRepository {
    override fun findMemberIdByMemberEmail(email: String): Long? {
        return queryFactory.select(QMember.member.memberId)
            .from(QMember.member)
            .where(
                QMember.member.email.eq(email)
                    .and(QMember.member.isDeleted.eq(IsDeleted.FALSE))
            )
            .fetchOne()
    }

    override fun existByEmail(email: String): Boolean {
        val count = queryFactory
            .selectOne()
            .from(QMember.member)
            .where(
                QMember.member.email.eq(email)
                    .and(QMember.member.isDeleted.eq(IsDeleted.FALSE))
            )
            .fetchFirst()

        return count != null
    }
}
