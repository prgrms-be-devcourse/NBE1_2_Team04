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
import team4.footwithme.stadium.domain.Stadium
import team4.footwithme.stadium.repository.CourtRepository
import team4.footwithme.stadium.repository.StadiumRepository
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest
import team4.footwithme.stadium.service.response.StadiumDetailResponse
import team4.footwithme.stadium.service.response.StadiumsResponse
import team4.footwithme.stadium.util.SortFieldMapper
import java.util.function.Function

@Service
class StadiumServiceImpl(
    private val stadiumRepository: StadiumRepository,
    private val memberRepository: MemberRepository,
    private val courtRepository: CourtRepository
) : StadiumService {
    override fun getStadiumList(page: Int?, sort: String?): Slice<StadiumsResponse> {
        val pageable: Pageable = PageRequest.of(page!!, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)))
        return stadiumRepository.findAllActiveStadiums(pageable)
            .map<StadiumsResponse>(Function<Stadium?, StadiumsResponse> { stadium: Stadium? ->
                StadiumsResponse.Companion.from(stadium)
            })
    }

    override fun getStadiumDetail(id: Long?): StadiumDetailResponse {
        return StadiumDetailResponse.Companion.from(
            findEntityByIdOrThrowException<Any>(
                stadiumRepository,
                id,
                ExceptionMessage.STADIUM_NOT_FOUND
            ) as Stadium
        )
    }

    override fun getStadiumsByName(query: String?, page: Int?, sort: String?): Slice<StadiumsResponse> {
        val pageable: Pageable = PageRequest.of(page!!, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)))
        return stadiumRepository.findByNameContainingIgnoreCase(query, pageable)
            .map<StadiumsResponse>(Function<Stadium?, StadiumsResponse> { stadium: Stadium? ->
                StadiumsResponse.Companion.from(stadium)
            })
    }

    override fun getStadiumsByAddress(address: String?, page: Int?, sort: String?): Slice<StadiumsResponse> {
        val pageable: Pageable = PageRequest.of(page!!, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)))
        return stadiumRepository.findByAddressContainingIgnoreCase(address, pageable)
            .map<StadiumsResponse>(Function<Stadium?, StadiumsResponse> { stadium: Stadium? ->
                StadiumsResponse.Companion.from(stadium)
            })
    }

    override fun getStadiumsWithinDistance(
        request: StadiumSearchByLocationServiceRequest?,
        page: Int?,
        sort: String?
    ): Slice<StadiumsResponse> {
        val pageable: Pageable = PageRequest.of(page!!, 10, Sort.by(SortFieldMapper.getDatabaseField(sort)))
        return stadiumRepository.findStadiumsByLocation(
            request!!.latitude!!,
            request.longitude!!,
            request.distance!!,
            pageable
        )
            .map<StadiumsResponse>(Function<Stadium, StadiumsResponse> { stadium: Stadium? ->
                StadiumsResponse.Companion.from(
                    stadium
                )
            })
    }

    @Transactional
    override fun registerStadium(request: StadiumRegisterServiceRequest?, member: Member?): StadiumDetailResponse {
        val stadium: Stadium = Stadium.Companion.create(
            member, request!!.name, request.address, request.phoneNumber, request.description,
            request.latitude, request.longitude
        )

        stadiumRepository.save(stadium)
        return StadiumDetailResponse.Companion.from(stadium)
    }

    @Transactional
    override fun updateStadium(
        request: StadiumUpdateServiceRequest?,
        member: Member?,
        stadiumId: Long?
    ): StadiumDetailResponse {
        val stadium = validateStadiumOwnership(member!!.memberId, stadiumId)
        stadium.updateStadium(
            member.memberId, request!!.name, request.address, request.phoneNumber, request.description,
            request.latitude!!, request.longitude!!
        )
        return StadiumDetailResponse.Companion.from(stadium)
    }

    @Transactional
    override fun deleteStadium(member: Member?, stadiumId: Long?) {
        val stadium = validateStadiumOwnership(member!!.memberId, stadiumId)
        stadium.deleteStadium(member.memberId)
        val courts = courtRepository.findActiveByStadiumId(stadiumId)
        if (!courts!!.isEmpty()) courtRepository.deleteAll(courts)
        stadiumRepository.delete(stadium)
    }

    private fun validateStadiumOwnership(memberId: Long?, stadiumId: Long?): Stadium {
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
        private val log: Logger = LoggerFactory.getLogger(StadiumServiceImpl::class.java)
    }
}
