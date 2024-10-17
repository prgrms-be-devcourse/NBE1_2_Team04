package team4.footwithme.stadium.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.global.exception.ExceptionMessage
import java.math.BigDecimal

@SQLDelete(sql = "UPDATE court SET is_deleted = 'TRUE' WHERE court_id = ?")
@Entity
class Court : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val courtId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    var stadium: Stadium? = null
        

    var name: @NotNull String? = null
        

    @Column(length = 200, nullable = true)
    var description: String? = null
        

    var pricePerHour: @NotNull BigDecimal? = null
        

    private constructor(stadium: Stadium?, name: String?, description: String?, pricePerHour: BigDecimal?) {
        this.stadium = stadium
        this.name = name
        this.description = description
        this.pricePerHour = pricePerHour
    }

    protected constructor()

    fun updateCourt(stadiumId: Long?, memberId: Long?, name: String?, description: String?, pricePerHour: BigDecimal?) {
        checkMember(memberId)
        checkStadium(stadiumId)
        this.name = name
        this.description = description
        this.pricePerHour = pricePerHour
    }

    fun deleteCourt(stadiumId: Long?, memberId: Long?) {
        checkMember(memberId)
        checkStadium(stadiumId)
    }

    private fun checkStadium(stadiumId: Long?) {
        require(stadium!!.stadiumId == stadiumId) { ExceptionMessage.COURT_NOT_OWNED_BY_STADIUM.text }
    }

    private fun checkMember(memberId: Long?) {
        require(stadium!!.member!!.memberId == memberId) { ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.text }
    }

    class CourtBuilder internal constructor() {
        private var stadium: Stadium? = null
        private var name: String? = null
        private var description: String? = null
        private var pricePerHour: BigDecimal? = null
        fun stadium(stadium: Stadium?): CourtBuilder {
            this.stadium = stadium
            return this
        }

        fun name(name: String?): CourtBuilder {
            this.name = name
            return this
        }

        fun description(description: String?): CourtBuilder {
            this.description = description
            return this
        }

        fun pricePerHour(pricePerHour: BigDecimal?): CourtBuilder {
            this.pricePerHour = pricePerHour
            return this
        }

        fun build(): Court {
            return Court(this.stadium, this.name, this.description, this.pricePerHour)
        }

        override fun toString(): String {
            return "Court.CourtBuilder(stadium=" + this.stadium + ", name=" + this.name + ", description=" + this.description + ", pricePerHour=" + this.pricePerHour + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(stadium: Stadium?, name: String?, description: String?, pricePerHour: BigDecimal?): Court {
            return builder()
                .stadium(stadium)
                .name(name)
                .description(description)
                .pricePerHour(pricePerHour)
                .build()
        }

        fun builder(): CourtBuilder {
            return CourtBuilder()
        }
    }
}
