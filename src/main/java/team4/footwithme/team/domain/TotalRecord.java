package team4.footwithme.team.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class TotalRecord {

    @NotNull
    private int winCount;

    @NotNull
    private int drawCount;

    @NotNull
    private int loseCount;

    @Builder
    private TotalRecord(int winCount, int drawCount, int loseCount) {
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
    }
}
