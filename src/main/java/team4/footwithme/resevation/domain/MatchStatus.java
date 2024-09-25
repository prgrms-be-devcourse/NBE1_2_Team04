package team4.footwithme.resevation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchStatus {
    PENDING,
    READY,
    IGNORE,
    PLAY,
    DONE
}
