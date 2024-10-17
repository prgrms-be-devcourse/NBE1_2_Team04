package team4.footwithme.team.domain

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull

@Embeddable
class TotalRecord
/**
 * 이 부분 질문
 */
{
    val winCount: @NotNull Int = 0

    val drawCount: @NotNull Int = 0

    val loseCount: @NotNull Int = 0

    class TotalRecordBuilder internal constructor() {
        fun build(): TotalRecord {
            return TotalRecord()
        }

        override fun toString(): String {
            return "TotalRecord.TotalRecordBuilder()"
        }
    }

    companion object {
        fun builder(): TotalRecordBuilder {
            return TotalRecordBuilder()
        }
    }
}
