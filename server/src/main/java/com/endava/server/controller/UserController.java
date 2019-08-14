package com.endava.server.controller;

import com.endava.server.dto.UserDTO;
import com.endava.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "{userId}")
    public UserDTO getUser(@RequestParam @Valid Long userId) {
       return userService.getUser(userId);
    }

    @PostMapping("/test")
    public UserDTO testApi(@RequestBody @Valid UserDTO userDTO){
        return userDTO;
    }

    @PostMapping(path = "/create")
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }
}