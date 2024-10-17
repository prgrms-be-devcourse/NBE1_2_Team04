package team4.footwithme.resevation.service.response

import team4.footwithme.member.domain.Member
import team4.footwithme.member.domain.MemberRole

@JvmRecord
data class ParticipantMemberInfo(
    val memberId: Long?,
    val email: String?,
    val name: String?,
    val memberRole: MemberRole?
) {
    constructor(member: Member?) : this(
        member!!.memberId,
        member.email,
        member.name,
        member.memberRole
    )
}
