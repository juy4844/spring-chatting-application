package com.websocket.chat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
public class ChatMessage {

    public ChatMessage() {
    }

    @Builder
    public ChatMessage(String message, long userCount) {
        this.message = message;
        this.userCount = userCount;
    }

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_join_id")
    private ChatJoin chatJoin;

    private String message; // 메시지
    @CreationTimestamp
    private Timestamp createDate;
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
}
