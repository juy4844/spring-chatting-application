package com.websocket.chat.controller;

import com.websocket.chat.repo.ChatRoomRepository;
import com.websocket.chat.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping({"","/index"})
    public String index() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        String token = jwtTokenProvider.generateToken(name);
        //chatRoomRepository.userJoin(token, name);
        //System.out.println("generate token");
        return "redirect:/chat/room";
    }

    @ResponseBody
    @GetMapping("/test")
    public String test() {
        int a = 0;
        for (int i =0; i<1000000; i++) {
            a += 1;
        }
        return "ok";
    }
}
