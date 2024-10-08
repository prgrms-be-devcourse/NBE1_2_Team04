package team4.footwithme.resevation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ParticipantGender {
    MALE("남성"),
    FEMALE("여성"),
    MIXED("혼성");

    private final String description;
}
