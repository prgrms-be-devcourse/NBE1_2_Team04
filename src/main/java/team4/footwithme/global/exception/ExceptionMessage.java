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
    COURT_NOT_OWNED_BY_STADIUM("해당 풋살장의 구장이 아닙니다."),


    // Member
    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),

    // Chat
    CHAT_NOT_FOUND("해당 채팅을 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND("해당 채팅방을 찾을 수 없습니다."),
    MEMBER_NOT_IN_CHATROOM("채팅방에 참여한 회원이 아닙니다."),
    MEMBER_IN_CHATROOM("해당 회원이 채팅방에 존재합니다."),
    UNAUTHORIZED_MESSAGE_EDIT("해당 메세지의 수정 권한이 없습니다."),



    // Reservation
    RESERVATION_NOT_FOUND("해당 예약을 찾을 수 없습니다."),
    RESERVATION_STATUS_NOT_READY("해당 예약은 준비 상태가 아닙니다."),
    RESERVATION_MEMBER_NOT_MATCH("예약자만이 예약을 신청할 수 있습니다."),
    RESERVATION_CONFLICT("해당 예약은 더 이상 사용할 수 없습니다."),
    RESERVATION_SUCCESS("예약에 성공했습니다."),


    //Game
    GAME_NOT_FOUND("해당 게임을 찾을 수 없습니다.")
    ;
    private final String text;
}
