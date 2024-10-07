package team4.footwithme.resevation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.CustomException;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.resevation.repository.ReservationRepository;
import team4.footwithme.resevation.service.response.ReservationsResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public Slice<ReservationsResponse> findReadyReservations(Long reservationId, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdAt"));
        Reservation reservation = (Reservation) findEntityByIdOrThrowException(reservationRepository, reservationId, ExceptionMessage.RESERVATION_NOT_FOUND);

        if (reservation.getReservationStatus() != ReservationStatus.READY) {
            throw new CustomException(ExceptionMessage.RESERVATION_STATUS_NOT_READY.getText());
        }

        return reservationRepository.findByMatchDateAndCourtAndReservationStatus(
                        reservation.getMatchDate(), reservation.getCourt(), ReservationStatus.READY, pageRequest)
                .map(ReservationsResponse::from);
    }



    private <T> T findEntityByIdOrThrowException(CustomGlobalRepository<T> repository, Long id, ExceptionMessage exceptionMessage) {
        return repository.findActiveById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, exceptionMessage);
                    return new IllegalArgumentException(exceptionMessage.getText());
                });
    }
}
