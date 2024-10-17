package team4.footwithme.vote.service.request

@JvmRecord
data class ChoiceCreateServiceRequest(
    val voteItemIds: List<Long?>?
) {
}
