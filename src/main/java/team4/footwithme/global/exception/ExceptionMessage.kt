package team4.footwithme.global.exception

import lombok.Getter

@Getter
enum class ExceptionMessage(@JvmField val text: String) {
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

    // Team
    MEMBER_NOT_IN_TEAM("해당 회원이 팀에 존재하지 않습니다."),

    // Reservation
    RESERVATION_NOT_FOUND("해당 매칭 예약을 찾을 수 없습니다."),
    RESERVATION_NOT_MEMBER("해당 매칭 예약 수정 권한이 없습니다."),
    RESERVATION_STATUS_NOT_READY("해당 예약은 준비 상태가 아닙니다."),
    RESERVATION_MEMBER_NOT_MATCH("예약자만이 예약을 신청할 수 있습니다."),
    RESERVATION_CONFLICT("해당 예약은 더 이상 사용할 수 없습니다."),
    RESERVATION_SUCCESS("예약에 성공했습니다."),
    PARTICIPANT_NOT_MEMBER("해당 매칭 예약의 참가 인원 수정 권한이 없습니다."),
    PARTICIPANT_NOT_IN_MEMBER("해당 회원이 매칭 예약에 존재하지 않습니다."),
    PARTICIPANT_IN_MEMBER("해당 회원이 매칭 예약에 이미 존재합니다."),
    MERCENARY_IN_RESERVATION("해당 회원은 이미 용병 신청을 했습니다."),
    SAME_PARTICIPANT_ROLE("참가자의 역할과 수정하려는 역할이 동일합니다."),

    // Mercenary
    MERCENARY_NOT_FOUND("해당 용병 게시판을 찾을 수 없습니다."),

    //Game
    GAME_NOT_FOUND("해당 게임을 찾을 수 없습니다."),
    GAME_STATUS_NOT_VALID("게임 상태는 READY 또는 IGNORE만 가능합니다.");
}
