package com.example.toyproject.chat.repository;

import com.example.toyproject.chat.dto.ChatDto;
import com.example.toyproject.chat.dto.QChatDto_ChatLogResponse;
import com.example.toyproject.chat.entity.Chat;
import com.example.toyproject.chat.entity.QChat;
import com.example.toyproject.chat.entity.QUserChat;
import com.example.toyproject.player.entity.QPlayer;
import com.example.toyproject.player.entity.QUserPlayer;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class UserChatQueryRepositoryImpl implements UserChatQueryRepository {

    private final QUserChat qUserChat = QUserChat.userChat;
    private final QChat qChat = QChat.chat;
    private final QUserPlayer qUserPlayer = QUserPlayer.userPlayer;
    private final QPlayer qPlayer = QPlayer.player;
    private final JPAQueryFactory queryFactory;

    @Override
    // userId는 프론트에서 보내는 값으로, 해당 유저가 보낸 채팅인지 확인하기 위해 사용합니다.
    public Page<ChatDto.ChatLogResponse> findChatLogPageByRoomIdAndClassifyIsMine(Long roomId, Long userId, Pageable pageable, Long startId) {
        JPAQuery<ChatDto.ChatLogResponse> query =
                queryFactory
                        .select(new QChatDto_ChatLogResponse(
                                qChat.id,
                                qUserPlayer.user.id,
                                qChat.message,
                                qUserPlayer.user.nickname,
                                qChat.createdAt,
                                isMine(userId)
                        ))
                        .from(qUserChat)
                        .join(qUserChat.chat, qChat)
                        .join(qUserChat.userPlayer, qUserPlayer)
                        .join(qUserPlayer.player, qPlayer)
                        .where(qChat.room.id.eq(roomId)
                                .and(gtChatId(startId)));

        // Pageable의 Sort 정보 적용
        if (pageable.getSort().isSorted()) {
            PathBuilder<Chat> pathBuilder = new PathBuilder<>(Chat.class, "chat");

            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                query.orderBy(new OrderSpecifier(direction, pathBuilder.get(order.getProperty())));
            }
        } else {
            // 기본 정렬
            query.orderBy(qChat.createdAt.desc());
        }

        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<ChatDto.ChatLogResponse> content = query.fetch();

        if (content.isEmpty()) {
            return Page.empty(pageable);
        }

        Long totalCount = queryFactory
                .select(qChat.count())
                .from(qUserChat)
                .join(qUserChat.chat, qChat)
                .join(qUserChat.userPlayer, qUserPlayer)
                .where(qChat.room.id.eq(roomId)
                        .and(qUserPlayer.user.id.eq(userId)))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression gtChatId(Long startId) {
        return startId != null ? qChat.id.loe(startId) : null;
    }

    private BooleanExpression isMine(Long userId) {
        return qUserPlayer.user.id.eq(userId);
    }

}
