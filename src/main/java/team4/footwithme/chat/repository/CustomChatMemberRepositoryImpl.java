package team4.footwithme.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.Member;

import static team4.footwithme.chat.domain.QChatMember.chatMember;
@RequiredArgsConstructor
public class CustomChatMemberRepositoryImpl implements CustomChatMemberRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean existByMemberAndChatroom(Member member, Chatroom chatroom) {
        Integer count = queryFactory
                .selectOne()
                .from(chatMember)
                .where(chatMember.isDeleted.eq(IsDeleted.FALSE)
                        .and(chatMember.chatRoom.eq(chatroom))
                        .and(chatMember.member.eq(member)))
                .fetchFirst();

        return count != null;
    }
}
