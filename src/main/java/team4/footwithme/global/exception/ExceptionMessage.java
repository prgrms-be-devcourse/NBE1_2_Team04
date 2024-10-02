package team4.footwithme.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    // Stadium
    STADIUM_NOT_FOUND("해당 풋살장을 찾을 수 없습니다."),
    STADIUM_NOT_OWNED_BY_MEMBER("본인이 소유한 풋살장이 아닙니다."),


    // Court
    COURT_NOT_FOUND("해당 구장을 찾을 수 없습니다."),


    // Member
    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),

    // Chat
    CHAT_NOT_FOUND("해당 채팅을 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND("해당 채팅방을 찾을 수 없습니다."),
    MEMBER_NOT_IN_CHATROOM("채팅방에 참여한 회원이 아닙니다."),
    MEMBER_IN_CHATROOM("해당 회원이 채팅방에 존재합니다."),
    UNAUTHORIZED_MESSAGE_EDIT("해당 메세지의 수정 권한이 없습니다.")


    ;
    private final String text;
}
