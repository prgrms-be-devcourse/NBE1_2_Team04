package team4.footwithme.stadium.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Position {

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Builder
    private Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updatePosition(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
