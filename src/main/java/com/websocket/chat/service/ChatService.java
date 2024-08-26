package com.websocket.chat.service;

import com.websocket.chat.dto.ChatMessageDto;
import com.websocket.chat.model.ChatJoin;
import com.websocket.chat.model.ChatMessage;
import com.websocket.chat.repo.ChatJoinRepository;
import com.websocket.chat.repo.ChatMessageRepository;
import com.websocket.chat.repo.ChatRoomRepository;
import com.websocket.chat.repo.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final RedisRepository redisRepository;
    private final ChatJoinRepository chatJoinRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessageDto chatMessage) {
        chatMessage.setUserCount(redisRepository.getUserCount(chatMessage.getRoomId()));
        if (ChatMessageDto.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessageDto.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    @Async
    @Transactional
    public void saveChatMessageAsync(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(chatMessageDto.getMessage());

        String topic = chatMessageDto.getRoomId();
        String[] parts = topic.split("_");
        Long roomId = Long.parseLong(parts[2]);

        System.out.println(chatMessageDto.getSender() + "  " + roomId);

        if (!chatJoinRepository.findByUsernameAndRoomId(chatMessageDto.getSender(), roomId).isPresent()) {
            System.out.println("오류");
            return;
        }
        ChatJoin chatJoin = chatJoinRepository.findByUsernameAndRoomId(chatMessageDto.getSender(), roomId).get();
        chatMessage.setChatJoin(chatJoin);
        chatMessageRepository.save(chatMessage);
    }

}
