package team4.footwithme.stadium.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.member.domain.Member

@SQLDelete(sql = "UPDATE stadium SET is_deleted = 'TRUE' WHERE stadium_id = ?")
@Entity
class Stadium : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val stadiumId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null
        

    @Column(nullable = false)
    var name: @NotNull String? = null
        

    var address: @NotNull String? = null
        

    var phoneNumber: @NotNull String? = null
        

    @Column(length = 200, nullable = true)
    var description: String? = null
        

    @Embedded
    var position: Position? = null
        

    private constructor(
        member: Member?,
        name: String?,
        address: String?,
        phoneNumber: String?,
        description: String?,
        position: Position?
    ) {
        this.member = member
        this.name = name
        this.address = address
        this.phoneNumber = phoneNumber
        this.description = description
        this.position = position
    }

    protected constructor()

    fun updateStadium(
        memberId: Long?,
        name: String?,
        address: String?,
        phoneNumber: String?,
        description: String?,
        latitude: Double,
        longitude: Double
    ) {
        checkMember(memberId)
        this.name = name
        this.address = address
        this.phoneNumber = phoneNumber
        this.description = description
        position!!.updatePosition(latitude, longitude)
    }

    fun deleteStadium(memberId: Long?) {
        checkMember(memberId)
    }

    fun createCourt(memberId: Long?) {
        checkMember(memberId)
    }

    private fun checkMember(memberId: Long?) {
        require(member!!.memberId == memberId) { ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.text }
    }

    class StadiumBuilder internal constructor() {
        private var member: Member? = null
        private var name: String? = null
        private var address: String? = null
        private var phoneNumber: String? = null
        private var description: String? = null
        private var position: Position? = null
        fun member(member: Member?): StadiumBuilder {
            this.member = member
            return this
        }

        fun name(name: String?): StadiumBuilder {
            this.name = name
            return this
        }

        fun address(address: String?): StadiumBuilder {
            this.address = address
            return this
        }

        fun phoneNumber(phoneNumber: String?): StadiumBuilder {
            this.phoneNumber = phoneNumber
            return this
        }

        fun description(description: String?): StadiumBuilder {
            this.description = description
            return this
        }

        fun position(position: Position?): StadiumBuilder {
            this.position = position
            return this
        }

        fun build(): Stadium {
            return Stadium(this.member, this.name, this.address, this.phoneNumber, this.description, this.position)
        }

        override fun toString(): String {
            return "Stadium.StadiumBuilder(member=" + this.member + ", name=" + this.name + ", address=" + this.address + ", phoneNumber=" + this.phoneNumber + ", description=" + this.description + ", position=" + this.position + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(
            member: Member?,
            name: String?,
            address: String?,
            phoneNumber: String?,
            description: String?,
            latitude: Double,
            longitude: Double
        ): Stadium {
            return builder()
                .member(member)
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .description(description)
                .position(
                    Position.Companion.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .build()
                )
                .build()
        }

        fun builder(): StadiumBuilder {
            return StadiumBuilder()
        }
    }
} //    @NotNull
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


