package team4.footwithme.team.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Embeddable
public class TotalRecord {

    @NotNull
    private int winCount;

    @NotNull
    private int drawCount;

    @NotNull
    private int loseCount;

    /**
     * 이 부분 질문
     */
    @Builder
    public TotalRecord() {
        this.winCount = 0;
        this.drawCount = 0;
        this.loseCount = 0;
    }
}
