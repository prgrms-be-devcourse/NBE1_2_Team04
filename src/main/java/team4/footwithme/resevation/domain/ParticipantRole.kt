package team4.footwithme.resevation.domain

import lombok.Getter

@Getter
enum class ParticipantRole {
    MEMBER,
    PENDING,
    ACCEPT,
    IGNORE
}
