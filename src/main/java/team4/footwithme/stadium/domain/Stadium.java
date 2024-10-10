package team4.footwithme.stadium.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE stadium SET is_deleted = 'TRUE' WHERE stadium_id = ?")
@Entity
public class Stadium extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stadiumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String phoneNumber;

    @Column(length = 200, nullable = true)
    private String description;

    @Embedded
    private Position position;

    @Builder
    private Stadium(Member member, String name, String address, String phoneNumber, String description, Position position) {
        this.member = member;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.position = position;
    }

    public static Stadium create(Member member, String name, String address, String phoneNumber, String description, double latitude, double longitude) {
        return Stadium.builder()
            .member(member)
            .name(name)
            .address(address)
            .phoneNumber(phoneNumber)
            .description(description)
            .position(Position.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build())
            .build();
    }

    public void updateStadium(Long memberId, String name, String address, String phoneNumber, String description, Double latitude, Double longitude) {
        checkMember(memberId);
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.position.updatePosition(latitude, longitude);
    }

    public void deleteStadium(Long memberId) {
        checkMember(memberId);
    }

    public void createCourt(Long memberId) {
        checkMember(memberId);
    }

    private void checkMember(Long memberId) {
        if (!this.member.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }
    }
}


//    @NotNull
//    @Column(columnDefinition = "POINT")
//    private Point position;


//    @Builder
//    public Stadium(Member member, String name, String address, String phoneNumber, String description, Point position) {
//        this.member = member;
//        this.name = name;
//        this.address = address;
//        this.phoneNumber = phoneNumber;
//        this.description = description;
//        this.position = position;
//    }
//
//    public static Stadium create(Member member, String name, String address, String phoneNumber, String description, double latitude, double longitude) {
//        Point position = PositionUtil.createPoint(latitude, longitude);
//        return Stadium.builder()
//                .member(member)
//                .name(name)
//                .address(address)
//                .phoneNumber(phoneNumber)
//                .description(description)
//                .position(position)
//                .build();
//    }
