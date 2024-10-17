package team4.footwithme.stadium.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Position {
    @Column(nullable = false)
    var latitude: Double = 0.0
        

    @Column(nullable = false)
    var longitude: Double = 0.0
        

    private constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }

    protected constructor()

    fun updatePosition(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }

    class PositionBuilder internal constructor() {
        private var latitude = 0.0
        private var longitude = 0.0
        fun latitude(latitude: Double): PositionBuilder {
            this.latitude = latitude
            return this
        }

        fun longitude(longitude: Double): PositionBuilder {
            this.longitude = longitude
            return this
        }

        fun build(): Position {
            return Position(this.latitude, this.longitude)
        }

        override fun toString(): String {
            return "Position.PositionBuilder(latitude=" + this.latitude + ", longitude=" + this.longitude + ")"
        }
    }

    companion object {
        fun builder(): PositionBuilder {
            return PositionBuilder()
        }
    }
}
