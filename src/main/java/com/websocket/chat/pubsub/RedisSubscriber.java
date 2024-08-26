package com.websocket.chat.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.dto.ChatMessageDto;

import com.websocket.chat.model.ChatJoin;
import com.websocket.chat.model.ChatMessage;
import com.websocket.chat.repo.ChatJoinRepository;
import com.websocket.chat.repo.ChatMessageRepository;
import com.websocket.chat.repo.UserRepository;
import com.websocket.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessageDto chatMessage = objectMapper.readValue(publishMessage, ChatMessageDto.class);

            //비동기적으로 메시지를 저장
            chatService.saveChatMessageAsync(chatMessage);

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }


}
