package com.websocket.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ChatRoom", schema = "chattingdatabase")
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;
    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long roomId;

    private String roomName;
    @CreationTimestamp
    private Timestamp sendDate;

    private long userCount; // 채팅방 인원수

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatJoin> chatJoins = new ArrayList<>();

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomName = name;
        return chatRoom;
    }
}
