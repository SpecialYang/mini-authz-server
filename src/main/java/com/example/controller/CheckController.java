package com.example.controller;

import com.example.common.RestResult;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author special.fy
 */
@RestController
public class CheckController {

    @Autowired
    UserService userService;

    @RequestMapping("/check/**")
    public RestResult<String> check(@RequestHeader(value = "authorization", required = false) String token,
                                    HttpServletRequest request) {
        return userService.check(token, request.getServletPath().substring("/check".length()));
    }
}
