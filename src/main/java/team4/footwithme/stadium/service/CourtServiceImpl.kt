package team4.footwithme.stadium.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.member.domain.Member
import team4.footwithme.member.repository.MemberRepository
import team4.footwithme.stadium.domain.Court
import team4.footwithme.stadium.domain.Stadium
import team4.footwithme.stadium.repository.CourtRepository
import team4.footwithme.stadium.repository.StadiumRepository
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest
import team4.footwithme.stadium.service.response.CourtDetailResponse
import team4.footwithme.stadium.service.response.CourtsResponse
import team4.footwithme.stadium.util.SortFieldMapper
import java.util.function.Function

@Service
class CourtServiceImpl(
    private val courtRepository: CourtRepository,
    private val stadiumRepository: StadiumRepository,
    private val memberRepository: MemberRepository
) : CourtService {
    override fun getCourtsByStadiumId(stadiumId: Long?, page: Int?, sort: String?): Slice<CourtsResponse> {
        findEntityByIdOrThrowException(stadiumRepository, stadiumId, ExceptionMessage.STADIUM_NOT_FOUND)
        val pageable: Pageable = PageRequest.of(page!!, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)))
        val courts = courtRepository.findByStadium_StadiumId(stadiumId, pageable)
        return courts!!.map<CourtsResponse>(Function<Court?, CourtsResponse> { court: Court? ->
            CourtsResponse.Companion.from(
                court
            )
        })
    }

    override fun getAllCourts(page: Int?, sort: String?): Slice<CourtsResponse> {
        val pageable: Pageable = PageRequest.of(page!!, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)))
        val courts = courtRepository.findAllActive(pageable)
        return courts!!.map<CourtsResponse>(Function<Court?, CourtsResponse> { court: Court? ->
            CourtsResponse.Companion.from(
                court
            )
        })
    }

    override fun getCourtByCourtId(courtId: Long?): CourtDetailResponse {
        val court = findEntityByIdOrThrowException(courtRepository, courtId, ExceptionMessage.COURT_NOT_FOUND) as Court
        return CourtDetailResponse.Companion.from(court)
    }

    @Transactional
    override fun registerCourt(request: CourtRegisterServiceRequest?, member: Member?): CourtDetailResponse {
        validateStadiumOwnership(request!!.stadiumId, member!!.memberId).createCourt(member.memberId)
        val court: Court = Court.Companion.create(
            findEntityByIdOrThrowException<Any>(
                stadiumRepository,
                request.stadiumId,
                ExceptionMessage.STADIUM_NOT_FOUND
            ) as Stadium?,
            request.name,
            request.description,
            request.price_per_hour
        )
        courtRepository.save(court)
        return CourtDetailResponse.Companion.from(court)
    }

    @Transactional
    override fun updateCourt(
        request: CourtUpdateServiceRequest?,
        member: Member?,
        courtId: Long?
    ): CourtDetailResponse {
        validateStadiumOwnership(request!!.stadiumId, member!!.memberId)
        val court = findEntityByIdOrThrowException(courtRepository, courtId, ExceptionMessage.COURT_NOT_FOUND) as Court
        court.updateCourt(request.stadiumId, member.memberId, request.name, request.description, request.price_per_hour)
        return CourtDetailResponse.Companion.from(court)
    }

    @Transactional
    override fun deleteCourt(request: CourtDeleteServiceRequest?, member: Member?, courtId: Long?) {
        validateStadiumOwnership(request!!.stadiumId, member!!.memberId)
        val court = findEntityByIdOrThrowException(courtRepository, courtId, ExceptionMessage.COURT_NOT_FOUND) as Court
        court.deleteCourt(request.stadiumId, member.memberId)
        courtRepository.delete(court)
    }

    private fun validateStadiumOwnership(stadiumId: Long?, memberId: Long?): Stadium {
        findEntityByIdOrThrowException(memberRepository, memberId, ExceptionMessage.MEMBER_NOT_FOUND)
        return findEntityByIdOrThrowException(
            stadiumRepository,
            stadiumId,
            ExceptionMessage.STADIUM_NOT_FOUND
        ) as Stadium
    }

    private fun <T> findEntityByIdOrThrowException(
        repository: CustomGlobalRepository<T?>,
        id: Long?,
        exceptionMessage: ExceptionMessage
    ): T? {
        return repository.findActiveById(id)
            .orElseThrow {
                log.warn(">>>> {} : {} <<<<", id, exceptionMessage)
                IllegalArgumentException(exceptionMessage.text)
            }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CourtServiceImpl::class.java)
    }
}
