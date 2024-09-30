package team4.footwithme.stadium.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourtExceptionMessage {
    COURT_NOT_FOUND("해당 구장을 찾을 수 없습니다.")

    ;
    private final String text;
}
