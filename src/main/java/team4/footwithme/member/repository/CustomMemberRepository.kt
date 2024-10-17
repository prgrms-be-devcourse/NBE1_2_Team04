package team4.footwithme.member.repository

interface CustomMemberRepository {
    fun findMemberIdByMemberEmail(email: String): Long?

    fun existByEmail(email: String): Boolean
}