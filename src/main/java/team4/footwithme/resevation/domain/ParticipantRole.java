package team4.footwithme.resevation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipantRole {
    MEMBER,
    PENDING,
    ACCEPT,
    IGNORE
}
