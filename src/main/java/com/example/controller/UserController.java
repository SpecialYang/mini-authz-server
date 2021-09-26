package com.example.controller;

import com.example.common.RestResult;
import com.example.model.IDToken;
import com.example.model.dto.UserDTO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author special.fy
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public RestResult<String> signup(@RequestBody UserDTO user) {
        return userService.signup(user);
    }

    @PostMapping("/signin")
    public RestResult<IDToken> signin(@RequestBody UserDTO user) {
        return userService.signin(user);
    }
}
