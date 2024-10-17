package team4.footwithme.resevation.domain

import lombok.Getter

@Getter
enum class ParticipantGender(val description: String) {
    MALE("남성"),
    FEMALE("여성"),
    MIXED("혼성");
}
