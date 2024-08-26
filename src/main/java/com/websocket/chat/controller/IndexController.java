package com.websocket.chat.controller;

import com.websocket.chat.repo.RedisRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RedisRepository redisRepository;
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
