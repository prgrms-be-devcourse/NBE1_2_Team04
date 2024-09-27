package team4.footwithme.chat.service.request;

// TODO Validation 구현하기
public record ChatMemberServiceRequest(
        Long chatroomId,
        Long memberId) {
}
