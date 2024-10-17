package team4.footwithme.vote.api.request

import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest

@JvmRecord
data class ChoiceCreateRequest(
    val voteItemIds: List<Long?>
) {
    fun toServiceRequest(): ChoiceCreateServiceRequest {
        return ChoiceCreateServiceRequest(
            voteItemIds
        )
    }
}
