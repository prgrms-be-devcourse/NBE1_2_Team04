package team4.footwithme.stadium.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Position {

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Position() {
    }

    public static PositionBuilder builder() {
        return new PositionBuilder();
    }

    public void updatePosition(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public static class PositionBuilder {
        private double latitude;
        private double longitude;

        PositionBuilder() {
        }

        public PositionBuilder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public PositionBuilder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Position build() {
            return new Position(this.latitude, this.longitude);
        }

        public String toString() {
            return "Position.PositionBuilder(latitude=" + this.latitude + ", longitude=" + this.longitude + ")";
        }
    }
}
