package team4.footwithme.resevation.service

import org.springframework.data.domain.Slice
import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest
import team4.footwithme.resevation.service.response.GameDetailResponse

interface GameService {
    fun registerGame(member: Member?, request: GameRegisterServiceRequest?): GameDetailResponse

    fun updateGameStatus(member: Member?, request: GameStatusUpdateServiceRequest?): String?

    fun findPendingGames(member: Member?, reservationId: Long?, page: Int?): Slice<GameDetailResponse>
}
