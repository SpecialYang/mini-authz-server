package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.common.Config;
import com.example.common.RestResult;
import com.example.exception.CustomException;
import com.example.model.IDToken;
import com.example.model.dto.UserDTO;
import com.example.model.entity.User;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;
import com.example.util.CodeCUtil;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author special.fy
 */
@Service
public class UserService {

    private static final String TOKEN_PREFIX = "Bearer ";
    @Autowired
    UserRepository userRepository;

    @Autowired
    Config config;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public RestResult<String> signup(UserDTO userDTO) {
        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(CodeCUtil.encodePassword(userDTO.getPassword(), config.getSalt()));
            user.setApis(JSON.toJSONString(userDTO.getApis()));

            userRepository.save(user);
            return RestResult.successResult(null, String.format("Create user %s success.", user.getUsername()));
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public RestResult<IDToken> signin(UserDTO userDTO) {
        if (!userRepository.existsByUsername(userDTO.getUsername())){
            return RestResult.failResult(HttpStatus.BAD_REQUEST.value(), "Username does not exist.");
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        String inputPassword = CodeCUtil.encodePassword(userDTO.getPassword(), config.getSalt());
        if (!user.getPassword().equals(inputPassword)) {
            return RestResult.failResult(HttpStatus.BAD_REQUEST.value(), "Password is wrong.");
        }

        String token = jwtTokenProvider.createToken(userDTO.getUsername());

        return RestResult.successResult(new IDToken(token));
    }

    public RestResult<String> check(String token, String originPath) {
        // Make sure it is not empty.
        if (StringUtils.isBlank(token)) {
            return RestResult.failResult(HttpStatus.FORBIDDEN.value(), "Missing token.");
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            return RestResult.failResult(HttpStatus.FORBIDDEN.value(), "Token must start with Bearer.");
        }

        String actualToken = token.substring(TOKEN_PREFIX.length());
        JwtClaims claims;
        try {
            claims = jwtTokenProvider.validateToken(actualToken);
        } catch (Exception e) {
            return RestResult.failResult(HttpStatus.FORBIDDEN.value(), "Token is invalid.");
        }

        String username = (String) claims.getClaimValue("username");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return RestResult.failResult(HttpStatus.FORBIDDEN.value(),
                    String.format("User %s is not registered.", username));
        }

        List<String> apis = JSON.parseArray(user.getApis(), String.class);
        if (!CollectionUtils.isEmpty(apis)) {
            for (String api : apis) {
                if (api.equals("*") || api.equals(originPath)) {
                    return RestResult.successResult(username, "Validate success.");
                }
            }
        }

        return RestResult.failResult(HttpStatus.FORBIDDEN.value(),
                String.format("Token is valid, but user %s deny to access api %s.", username, originPath));
    }
}
