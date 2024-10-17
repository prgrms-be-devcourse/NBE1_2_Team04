package team4.footwithme.member.domain

import lombok.Getter

@Getter
enum class MemberRole(@JvmField val text: String) {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MERCHANT("ROLE_MERCHANT")
}
