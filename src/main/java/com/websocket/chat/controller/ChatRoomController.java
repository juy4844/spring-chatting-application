package com.websocket.chat.controller;

import com.websocket.chat.dto.ChatRoomDto;
import com.websocket.chat.dto.UserDto;
import com.websocket.chat.jwt.JwtTokenProvider;
import com.websocket.chat.model.ChatJoin;
import com.websocket.chat.model.ChatRoom;
import com.websocket.chat.model.LoginInfo;
import com.websocket.chat.model.User;
import com.websocket.chat.repo.ChatJoinRepository;
import com.websocket.chat.repo.ChatRoomRepository;
import com.websocket.chat.repo.RedisRepository;
import com.websocket.chat.repo.UserRepository;
import com.websocket.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;


    @GetMapping("/room")
    public String room() {
        return "/chat/newroom";
    }

    @GetMapping("/myroom")
    public String myroom() {
        return "/chat/myroom";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomDto> rooms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<ChatRoomDto> chatRoomDtos = chatRooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return chatRoomDtos;
    }

    @GetMapping("/myrooms")
    @ResponseBody
    public List<ChatRoomDto> myrooms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        List<ChatRoom> chatRooms = chatRoomRepository.findByUser(name);
        List<ChatRoomDto> chatRoomDtos = chatRooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return chatRoomDtos;
    }

    private ChatRoomDto convertToDto(ChatRoom chatRoom) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setRoomId("CHAT_ROOM_" + chatRoom.getRoomId());
        chatRoomDto.setName(chatRoom.getRoomName());
        chatRoomDto.setUserCount(chatRoom.getUserCount());
        return chatRoomDto;
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoomDto createRoom(@RequestParam String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return chatRoomService.createChatRoom(name, username);
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/room/join/{roomId}")
    public String roomJoin(Model model, @PathVariable String roomId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        chatRoomService.joinChatRoom(roomId, username);
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoomDto roomInfo(@PathVariable String roomId) {
        String[] parts = roomId.split("_");
        Long Id = Long.parseLong(parts[2]);
        ChatRoom chatRoom = chatRoomService.findOne(Id);
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setRoomId(roomId);
        chatRoomDto.setName(chatRoom.getRoomName());
        return chatRoomDto;
    }

    @GetMapping("/room/{roomId}/users")
    @ResponseBody
    public List<UserDto> users(@PathVariable String roomId) {

        List<User> users = chatRoomService.findUserByRoomId(roomId);
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
        for (UserDto userDto : userDtos) {
            System.out.println(userDto.getUsername());
        }
        return userDtos;
    }



}