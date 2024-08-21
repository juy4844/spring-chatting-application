package com.websocket.chat.repo;

import com.websocket.chat.model.ChatJoin;
import com.websocket.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ChatJoinRepository {
    private final EntityManager em;

    public void save(ChatJoin chatJoin) {
        em.persist(chatJoin);
    }

    public ChatJoin findByUsernameAndRoomId(String username, Long roomId) {
        return em.createQuery("select j from ChatJoin j where j.user.username = :username AND j.chatRoom.roomId = :roomId", ChatJoin.class)
                .setParameter("username", username)
                .setParameter("roomId", roomId)
                .getSingleResult();
    }
}
