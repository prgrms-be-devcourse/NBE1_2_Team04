package team4.footwithme.vote.domain;

import lombok.Getter;

@Getter
public enum VoteStatus {
    OPENED("진행 중"), CLOSED("종료");

    private final String text;

    VoteStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
