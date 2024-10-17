package team4.footwithme.resevation.service

import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.domain.Mercenary
import team4.footwithme.resevation.domain.Reservation
import team4.footwithme.resevation.repository.MercenaryRepository
import team4.footwithme.resevation.repository.ReservationRepository
import team4.footwithme.resevation.service.request.MercenaryServiceRequest
import team4.footwithme.resevation.service.response.MercenaryResponse
import java.time.format.DateTimeFormatter

@Service
class MercenaryServiceImpl(
    private val mercenaryRepository: MercenaryRepository,
    private val reservationRepository: ReservationRepository
) : MercenaryService {
    /**
     * 용병 게시판 생성
     * 예약자만 생성 가능
     */
    @Transactional
    override fun createMercenary(request: MercenaryServiceRequest?, member: Member?): MercenaryResponse {
        val reservation = getReservationByReservationId(request!!.reservationId)

        checkReservationCreatedBy(reservation, member)

        return MercenaryResponse(
            mercenaryRepository.save<Mercenary>(
                Mercenary.Companion.create(
                    reservation, makeDescription(
                        request.description, reservation
                    )
                )
            )
        )
    }

    /**
     * 단일 용병 게시판 조회
     */
    @Transactional(readOnly = true)
    override fun getMercenary(mercenaryId: Long?): MercenaryResponse {
        val mercenary = getMercenaryByMercenaryId(mercenaryId)

        return MercenaryResponse(mercenary)
    }

    /**
     * 용병 게시판 리스트 조회
     * 해당 용병 게시판의 예약 상태가 RECRUITING 인것만 조회
     * 리스트 및 페이징
     */
    @Transactional(readOnly = true)
    override fun getMercenaries(pageable: Pageable?): Page<MercenaryResponse> {
        val mercenaries = mercenaryRepository.findAllToPage(pageable!!)
        return mercenaries!!.map { mercenary: Mercenary? -> MercenaryResponse(mercenary) }
    }

    /**
     * 용병 게시판 삭제
     * 팀장만 삭제 가능
     */
    @Transactional
    override fun deleteMercenary(mercenaryId: Long?, member: Member?): Long? {
        val mercenary = getMercenaryByMercenaryId(mercenaryId)

        checkReservationCreatedBy(mercenary.reservation, member)

        mercenaryRepository.delete(mercenary)
        return mercenary.mercenaryId
    }


    private fun makeDescription(description: String, reservation: Reservation?): String {
        return reservation!!.matchDate!!.format(DateTimeFormatter.ofPattern("'('MM'/'dd HH':'mm')'")) +
                "(" +
                reservation.court!!.stadium!!.name +
                ") " +
                description
    }

    private fun getReservationByReservationId(reservationId: Long?): Reservation? {
        return reservationRepository.findByReservationId(reservationId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.RESERVATION_NOT_FOUND.text) }
    }

    private fun getMercenaryByMercenaryId(mercenaryId: Long?): Mercenary {
        return mercenaryRepository.findByMercenaryId(mercenaryId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.MERCENARY_NOT_FOUND.text) }
    }

    private fun checkReservationCreatedBy(reservation: Reservation?, member: Member?) {
        require(reservation!!.member!!.memberId == member!!.memberId) { ExceptionMessage.RESERVATION_NOT_MEMBER.text }
    }
}
