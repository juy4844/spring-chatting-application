package com.websocket.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;

    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
