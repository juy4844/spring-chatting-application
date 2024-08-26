package com.websocket.chat.repo;

import com.websocket.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final EntityManager em;

    public void save(ChatRoom chatRoom) {
        em.persist(chatRoom);
    }

    public ChatRoom findOne(Long id) {
        return em.find(ChatRoom.class, id);
    }

    public List<ChatRoom> findAll() {
        return em.createQuery("select r from ChatRoom r", ChatRoom.class)
                .getResultList();
    }

    public List<ChatRoom> findByUser(String username) {
        return em.createQuery("select c from ChatRoom c join ChatJoin cj on cj.chatRoom.id = c.id join cj.user u where u.username = :username", ChatRoom.class)
                .setParameter("username", username)
                .getResultList();
    }

}
