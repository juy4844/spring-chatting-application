package com.websocket.chat.controller;


import com.websocket.chat.dto.ChatMessageDto;
import com.websocket.chat.jwt.JwtTokenProvider;
import com.websocket.chat.repo.RedisRepository;
import com.websocket.chat.service.ChatService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisRepository redisRepository;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;
    //private final HttpSession session;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        // 채팅방 인원수 세팅
        message.setUserCount(redisRepository.getUserCount(message.getRoomId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatService.sendChatMessage(message);
    }
}
