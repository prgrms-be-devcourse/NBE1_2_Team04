package team4.footwithme.resevation.domain

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity

@SQLDelete(sql = "UPDATE mercenary SET is_deleted = 'TRUE' WHERE mercenary_id = ?")
@Entity
class Mercenary : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val mercenaryId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    var reservation: Reservation? = null
        

    @Column(length = 200, nullable = true)
    var description: String? = null
        

    private constructor(reservation: Reservation?, description: String?) {
        this.description = description
        this.reservation = reservation
    }

    protected constructor()

    class MercenaryBuilder internal constructor() {
        private var reservation: Reservation? = null
        private var description: String? = null
        fun reservation(reservation: Reservation?): MercenaryBuilder {
            this.reservation = reservation
            return this
        }

        fun description(description: String?): MercenaryBuilder {
            this.description = description
            return this
        }

        fun build(): Mercenary {
            return Mercenary(this.reservation, this.description)
        }

        override fun toString(): String {
            return "Mercenary.MercenaryBuilder(reservation=" + this.reservation + ", description=" + this.description + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(reservation: Reservation?, description: String?): Mercenary {
            return builder()
                .reservation(reservation)
                .description(description)
                .build()
        }

        fun createDefault(reservation: Reservation?): Mercenary {
            return builder()
                .reservation(reservation)
                .description("기본 설명")
                .build()
        }

        fun builder(): MercenaryBuilder {
            return MercenaryBuilder()
        }
    }
}
