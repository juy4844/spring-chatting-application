package com.websocket.chat.controller;

import com.websocket.chat.model.User;
import com.websocket.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public String addForm(@ModelAttribute("user") User user) {
        return "members/addMemberForm";
    }

    //    @PostMapping("/join")
//    public String save(@Valid @ModelAttribute UserDto userdto, BindingResult bindingResult) {
//        User user = new User(userdto.getUsername(), userdto.getLoginId(), userdto.getPassword());
//
//        userService.save(user);
//        return "redirect:/login";
//    }
    @PostMapping("/join")
    public String signup(@Valid @ModelAttribute User user) {
        userService.join(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") User user) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute LoginDto loginDto, BindingResult bindingResult) {
//        userService.login(loginDto);
//        return "redirect:/chat/room";
//    }
}
