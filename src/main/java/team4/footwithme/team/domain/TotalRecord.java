package team4.footwithme.team.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

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
    public TotalRecord() {
        this.winCount = 0;
        this.drawCount = 0;
        this.loseCount = 0;
    }

    public static TotalRecordBuilder builder() {
        return new TotalRecordBuilder();
    }

    public @NotNull int getWinCount() {
        return this.winCount;
    }

    public @NotNull int getDrawCount() {
        return this.drawCount;
    }

    public @NotNull int getLoseCount() {
        return this.loseCount;
    }

    public static class TotalRecordBuilder {
        TotalRecordBuilder() {
        }

        public TotalRecord build() {
            return new TotalRecord();
        }

        public String toString() {
            return "TotalRecord.TotalRecordBuilder()";
        }
    }
}
