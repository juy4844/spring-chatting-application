package com.websocket.chat.service;

import com.websocket.chat.dto.ChatRoomDto;
import com.websocket.chat.model.ChatJoin;
import com.websocket.chat.model.ChatRoom;
import com.websocket.chat.model.User;
import com.websocket.chat.repo.ChatJoinRepository;
import com.websocket.chat.repo.ChatRoomRepository;
import com.websocket.chat.repo.RedisRepository;
import com.websocket.chat.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final RedisRepository redisRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatJoinRepository chatJoinRepository;

    public ChatRoomDto createChatRoom(String name, String username) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(name);
        chatRoomRepository.save(chatRoom);

//        User user = userRepository.findByUsername(username).get();
//        ChatJoin chatJoin = new ChatJoin();
//        chatJoin.setUser(user);
//        chatJoin.setChatRoom(chatRoom);
//        chatJoinRepository.save(chatJoin);

        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setName(name);
        chatRoomDto.setRoomId("CHAT_ROOM_" + chatRoom.getRoomId());
        return chatRoomDto;
    }

    public void joinChatRoom(String roomId, String username) {
        User user = userRepository.findByUsername(username).get();
        String[] parts = roomId.split("_");
        Long id = Long.parseLong(parts[2]);

        if (chatJoinRepository.findByUsernameAndRoomId(username, id).isPresent()) {
            return;
        }
        ChatRoom chatRoom = chatRoomRepository.findOne(id);
        ChatJoin chatJoin = new ChatJoin();
        chatJoin.setUser(user);
        chatJoin.setChatRoom(chatRoom);
        chatJoinRepository.save(chatJoin);
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
        chatRoomRepository.save(chatRoom);
    }

    public List<User> findUserByRoomId(String roomId) {
        String[] parts = roomId.split("_");
        Long id = Long.parseLong(parts[2]);
        return chatJoinRepository.findUserByRoomId(id);
    }

    public ChatRoom findOne(Long roomId) {
        return chatRoomRepository.findOne(roomId);
    }
}
