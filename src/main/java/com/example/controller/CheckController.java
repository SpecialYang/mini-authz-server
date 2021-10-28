package com.example.controller;

import com.example.common.RestResult;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Be careful about http code.
     * It should return code 200 only when check token and apis pass.
     * Otherwise, it should return 403.
     */
    @RequestMapping("/check/**")
    public ResponseEntity<RestResult<String>> check(@RequestHeader(value = "authorization", required = false) String token,
                                HttpServletRequest request) {
        RestResult<String> result = userService.check(token, request.getServletPath().substring("/check".length()));
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
