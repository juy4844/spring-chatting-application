package com.websocket.chat.repo;

import com.websocket.chat.model.ChatJoin;
import com.websocket.chat.model.ChatRoom;
import com.websocket.chat.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatJoinRepository {
    private final EntityManager em;

    public void save(ChatJoin chatJoin) {
        em.persist(chatJoin);
    }

    public Optional<ChatJoin> findByUsernameAndRoomId(String username, Long roomId) {
        return em.createQuery("select j from ChatJoin j where j.user.username = :username AND j.chatRoom.roomId = :roomId", ChatJoin.class)
                .setParameter("username", username)
                .setParameter("roomId", roomId)
                .getResultList()
                .stream()
                .findFirst();
    }

    public List<User> findUserByRoomId(Long roomId) {
        return em.createQuery("SELECT cj.user FROM ChatJoin cj WHERE cj.chatRoom.id = :roomId")
                .setParameter("roomId", roomId)
                .getResultList();
    }
}
