package team4.footwithme.member.service;

import team4.footwithme.member.service.request.JoinCreateServiceRequest;
import team4.footwithme.member.service.response.MemberResponse;

public interface MemberService {
    MemberResponse join(JoinCreateServiceRequest serviceRequest);
}
