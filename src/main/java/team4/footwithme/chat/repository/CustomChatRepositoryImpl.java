package team4.footwithme.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.global.domain.IsDeleted;

import java.util.List;

import static team4.footwithme.chat.domain.QChat.chat;

@RequiredArgsConstructor
public class CustomChatRepositoryImpl implements CustomChatRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Slice<ChatResponse> findChatByChatroom(Chatroom chatroom, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        List<Chat> chats = getChatList(chatroom, pageable);
        boolean hasNext = false;
        if (chats.size() > pageSize) {
            chats.remove(pageSize);
            hasNext = true;
        }

        List<ChatResponse> content = chats.stream().map(ChatResponse::new).toList();
//        Long count = getCount(chatroom);

        return new SliceImpl<>(content, pageable, hasNext);
    }

    //Page<> 형태로 반환할 때 PageImpl에 사용
    private Long getCount(Chatroom chatroom) {
        return queryFactory
            .select(chat.count())
            .from(chat)
            .where(chat.isDeleted.eq(IsDeleted.FALSE)
                .and(chat.chatRoom.eq(chatroom))
            )
            .fetchOne();
    }

    private List<Chat> getChatList(Chatroom chatroom, Pageable pageable) {
        return queryFactory
            .select(chat)
            .from(chat)
            .where(chat.isDeleted.eq(IsDeleted.FALSE)
                .and(chat.chatRoom.eq(chatroom))
            )
            .orderBy(chat.createdAt.desc())
            .offset(pageable.getOffset())   // 페이지 번호
            .limit(pageable.getPageSize() + 1)  // 페이지 사이즈
            .fetch();
    }

}
