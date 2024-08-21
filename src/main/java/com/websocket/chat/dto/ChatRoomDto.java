package com.websocket.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDto {
    private String roomId;
    private String name;
    private long userCount; // 채팅방 인원수
}
