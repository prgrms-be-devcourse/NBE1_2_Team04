package team4.footwithme.resevation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.service.request.MercenaryServiceRequest;
import team4.footwithme.resevation.service.response.MercenaryResponse;

public interface MercenaryService {
    MercenaryResponse createMercenary(MercenaryServiceRequest request, Member member);

    MercenaryResponse getMercenary(Long mercenaryId);

    Page<MercenaryResponse> getMercenaries(Pageable pageable);

    Long deleteMercenary(Long mercenaryId, Member member);
}
