package team4.footwithme.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum TermsAgreed {
    AGREE,
    DISAGREE;

    private TermsAgreed() {
    }
}
