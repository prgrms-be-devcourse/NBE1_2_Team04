package team4.footwithme.vote.service

import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.member.domain.Member
import team4.footwithme.stadium.repository.CourtRepository
import team4.footwithme.team.repository.TeamRepository
import team4.footwithme.vote.domain.*
import team4.footwithme.vote.repository.ChoiceRepository
import team4.footwithme.vote.repository.VoteItemRepository
import team4.footwithme.vote.repository.VoteRepository
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest
import team4.footwithme.vote.service.request.VoteCourtCreateServiceRequest
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest
import team4.footwithme.vote.service.response.AllVoteResponse
import team4.footwithme.vote.service.response.VoteItemResponse
import team4.footwithme.vote.service.response.VoteResponse
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledFuture
import java.util.function.Function

@Service
class VoteServiceImpl(
    private val voteRepository: VoteRepository,
    private val voteItemRepository: VoteItemRepository,
    private val courtRepository: CourtRepository,
    private val teamRepository: TeamRepository,
    private val choiceRepository: ChoiceRepository,
    private val taskScheduler: TaskScheduler,
    private val eventPublisher: ApplicationEventPublisher
) : VoteService {
    private val scheduledTasks: MutableMap<Long?, ScheduledFuture<*>> = ConcurrentHashMap()

    @PostConstruct
    private fun initializeScheduledTasks() {
        val activeVotes = voteRepository.findOpenedVotes()
        for (vote in activeVotes!!) {
            addVoteTaskToTaskSchedule(vote)
        }
    }

    @Transactional
    override fun createCourtVote(request: VoteCourtCreateServiceRequest?, teamId: Long, member: Member?): VoteResponse {
        val memberId = member!!.memberId
        validateTeamByTeamId(teamId)

        val vote: Vote = Vote.Companion.create(memberId, teamId, request!!.title, request.endAt)
        val savedVote = voteRepository.save(vote)
        addVoteTaskToTaskSchedule(savedVote)

        val voteItemLocates = createVoteItemLocate(request, savedVote)

        val savedVoteItems = voteItemRepository.saveAll(voteItemLocates)
        val voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems)
        return VoteResponse.Companion.of(vote, voteItemResponses)
    }

    @Transactional
    override fun createDateVote(request: VoteDateCreateServiceRequest?, teamId: Long, member: Member?): VoteResponse {
        val memberId = member!!.memberId
        validateTeamByTeamId(teamId)

        val vote: Vote = Vote.Companion.create(memberId, teamId, request!!.title, request.endAt)
        val savedVote = voteRepository.save(vote)
        addVoteTaskToTaskSchedule(savedVote)

        val savedVoteItems = createVoteItemDate(request, savedVote)

        val voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems)
        return VoteResponse.Companion.of(savedVote, voteItemResponses)
    }

    override fun getVote(voteId: Long?): VoteResponse {
        val vote = getVoteByVoteId(voteId)
        val voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems())
        return VoteResponse.Companion.of(vote, voteItemResponses)
    }

    @Transactional
    override fun deleteVote(voteId: Long?, member: Member?): Long? {
        val vote = getVoteByVoteId(voteId)
        vote.checkWriterFromMemberId(member!!.memberId)
        cancelTaskInSchedulerFromVoteId(voteId)
        voteRepository.delete(vote)
        return voteId
    }

    @Transactional
    override fun createChoice(request: ChoiceCreateServiceRequest?, voteId: Long?, member: Member?): VoteResponse {
        val memberId = member!!.memberId
        val vote = getVoteByVoteId(voteId)

        val choices = createChoice(request, memberId)
        choiceRepository.saveAll(choices)

        val voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems())
        return VoteResponse.Companion.of(vote, voteItemResponses)
    }

    @Transactional
    override fun deleteChoice(voteId: Long?, member: Member?): VoteResponse {
        val memberId = member!!.memberId
        val vote = getVoteByVoteId(voteId)

        val choices = choiceRepository.findByMemberIdAndVoteId(memberId, voteId)

        choiceRepository.deleteAllInBatch(choices)
        val voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems())
        return VoteResponse.Companion.of(vote, voteItemResponses)
    }

    @Transactional
    override fun updateVote(serviceRequest: VoteUpdateServiceRequest?, voteId: Long?, member: Member?): VoteResponse {
        val memberId = member!!.memberId
        val vote = getVoteByVoteId(voteId)

        vote.update(serviceRequest!!.title, serviceRequest.endAt, memberId)
        cancelTaskInSchedulerFromVoteId(voteId)
        addVoteTaskToTaskSchedule(vote)

        val voteItemResponses = convertVoteItemsToResponseFrom(vote.getVoteItems())
        return VoteResponse.Companion.of(vote, voteItemResponses)
    }

    @Transactional
    override fun closeVote(voteId: Long?, member: Member?): VoteResponse {
        val vote = voteRepository.findNotDeletedVoteById(voteId)
            .orElseThrow { IllegalArgumentException("해당하는 투표가 없습니다.") }
        vote.checkWriterFromMemberId(member!!.memberId)
        if (vote.getVoteItems()[0] is VoteItemDate) {
            makeReservation(vote)
            log.info("makeReservation 실행됐음")
        }
        vote.updateVoteStatusToClose()
        log.info("updateVoteStatusToClose 실행됐음")
        cancelTaskInSchedulerFromVoteId(voteId)
        return VoteResponse.Companion.of(vote, convertVoteItemsToResponseFrom<VoteItem?>(vote.getVoteItems()))
    }

    override fun getAllVotes(teamId: Long): List<AllVoteResponse>? {
        validateTeamByTeamId(teamId)
        val votes = voteRepository.findAllByTeamId(teamId)
        return votes!!.stream()
            .map<AllVoteResponse>(Function<Vote?, AllVoteResponse> { vote: Vote? -> AllVoteResponse.Companion.from(vote) })
            .toList()
    }

    private fun createVoteItemDate(request: VoteDateCreateServiceRequest?, savedVote: Vote): List<VoteItemDate> {
        return voteItemRepository.saveAll<VoteItemDate>(
            request!!.choices.stream()
                .map<VoteItemDate>(Function<LocalDateTime, VoteItemDate> { choice: LocalDateTime? ->
                    VoteItemDate.Companion.create(
                        savedVote,
                        choice
                    )
                })
                .toList()
        )
    }

    private fun createVoteItemLocate(request: VoteCourtCreateServiceRequest?, savedVote: Vote): List<VoteItemLocate> {
        checkDuplicateStadiumIds(request!!.courtIds)
        return request.courtIds.stream()
            .map<VoteItemLocate>(Function<Long?, VoteItemLocate> { stadiumId: Long? ->
                VoteItemLocate.Companion.create(
                    savedVote,
                    stadiumId
                )
            })
            .toList()
    }

    private fun checkDuplicateStadiumIds(requestStadiumIds: List<Long?>) {
        require(courtRepository.countCourtByCourtIds(requestStadiumIds) == requestStadiumIds.size.toLong()) { "존재하지 않는 구장이 포함되어 있습니다." }
    }

    private fun createChoice(request: ChoiceCreateServiceRequest?, memberId: Long?): List<Choice> {
        return request!!.voteItemIds!!.stream()
            .map<Choice>(Function<Long?, Choice> { voteItemId: Long? -> Choice.Companion.create(memberId, voteItemId) })
            .toList()
    }

    private fun getVoteByVoteId(voteId: Long?): Vote {
        return voteRepository.findNotDeletedVoteById(voteId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 투표입니다.") }
    }

    private fun validateTeamByTeamId(teamId: Long) {
        require(teamRepository.existsById(teamId)) { "존재하지 않는 팀입니다." }
    }

    private fun <T : VoteItem?> convertVoteItemsToResponseFrom(voteItems: List<T>?): List<VoteItemResponse> {
        return voteItems!!.stream()
            .map { voteItem: T -> this.convertVoteItemToResponse(voteItem) }
            .toList()
    }


    private fun <T : VoteItem?> convertVoteItemToResponse(voteItem: T): VoteItemResponse {
        if (voteItem is VoteItemLocate) {
            return convertToVoteItemResponseFrom(voteItem)
        }
        if (voteItem is VoteItemDate) {
            return convertToVoteItemResponseFrom(voteItem)
        }
        throw IllegalArgumentException("지원하지 않는 투표 항목입니다.")
    }

    private fun convertToVoteItemResponseFrom(voteItemDate: VoteItemDate): VoteItemResponse {
        return VoteItemResponse.Companion.of(
            voteItemDate.voteItemId,
            voteItemDate.time.toString(),
            choiceRepository.findMemberIdsByVoteItemId(voteItemDate.voteItemId)
        )
    }

    private fun convertToVoteItemResponseFrom(voteItemLocate: VoteItemLocate): VoteItemResponse {
        return VoteItemResponse.Companion.of(
            voteItemLocate.voteItemId,
            courtRepository.findCourtNameByCourtId(voteItemLocate.courtId),
            choiceRepository.findMemberIdsByVoteItemId(voteItemLocate.voteItemId)
        )
    }

    private fun addVoteTaskToTaskSchedule(vote: Vote?) {
        val scheduledTask = taskScheduler.schedule(publishClosedVoteTask(vote!!.voteId), vote.instantEndAt)
        scheduledTasks[vote!!.voteId] = scheduledTask
    }

    private fun publishClosedVoteTask(voteId: Long?): Runnable {
        return Runnable { eventPublisher.publishEvent(RegisteredVoteEvent(voteId)) }
    }

    private fun cancelTaskInSchedulerFromVoteId(voteId: Long?) {
        val scheduledTask = scheduledTasks[voteId]
            ?: throw IllegalArgumentException("해당하는 투표 ID로 등록된 작업이 없습니다.")
        scheduledTask.cancel(false)
        scheduledTasks.remove(voteId)
    }

    override fun makeReservation(vote: Vote) {
        val voteItemDateId = choiceRepository.maxChoiceCountByVoteId(vote.voteId)
        val memberIds = choiceRepository.findMemberIdsByVoteItemId(voteItemDateId)
        val voteItemDate = vote.getVoteItems().stream()
            .filter { voteItem: VoteItem? -> voteItem!!.voteItemId == voteItemDateId }
            .map { obj: VoteItem? -> VoteItemDate::class.java.cast(obj) }
            .findFirst()
        val memberId = vote.memberId

        require(!voteItemDate.isEmpty) { "해당하는 일정이 없습니다." }
        val matchDate = voteItemDate.get().time
        val teamId = vote.teamId

        val locateVote = voteRepository.findRecentlyVoteByTeamId(teamId)
        val voteItemLocateId = choiceRepository.maxChoiceCountByVoteId(locateVote.voteId)
        val voteItemLocate = locateVote!!.getVoteItems().stream()
            .filter { voteItem: VoteItem? -> voteItem!!.voteItemId == voteItemLocateId }
            .map { obj: VoteItem? -> VoteItemLocate::class.java.cast(obj) }
            .findFirst()
        require(!voteItemLocate.isEmpty) { "해당하는 구장 아이디가 없습니다." }
        val courtId = voteItemLocate.get().courtId

        eventPublisher.publishEvent(
            EndVoteEvent(
                courtId,  // 투표에서 선택 된 코트ID
                memberId,  // 투표 생성자
                teamId,  // 팀 ID
                matchDate,  // 투표에서 선택 된 일정
                memberIds // 투표 참여자
            )
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(VoteServiceImpl::class.java)
    }
}
