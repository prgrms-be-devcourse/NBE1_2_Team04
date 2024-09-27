package team4.footwithme.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.member.service.request.JoinCreateServiceRequest;
import team4.footwithme.member.service.response.MemberResponse;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponse join(JoinCreateServiceRequest serviceRequest) {
        if(memberRepository.existByEmail(serviceRequest.email()))
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");

        Member member = serviceRequest.toEntity();
        memberRepository.save(member);


        return MemberResponse.from(member);

    }
}
