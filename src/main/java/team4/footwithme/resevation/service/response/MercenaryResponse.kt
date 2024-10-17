package team4.footwithme.resevation.service.response

import team4.footwithme.resevation.domain.Mercenary

@JvmRecord
data class MercenaryResponse(
    val mercenaryId: Long?,
    val reservationId: Long?,
    val description: String?
) {
    constructor(mercenary: Mercenary?) : this(
        mercenary!!.mercenaryId,
        mercenary.reservation!!.reservationId,
        mercenary.description
    )

}
