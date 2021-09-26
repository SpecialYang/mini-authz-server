package com.example.service;

import com.example.common.Config;
import com.example.common.RestResult;
import com.example.exception.CustomException;
import com.example.model.IDToken;
import com.example.model.dto.UserDTO;
import com.example.model.entity.User;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;
import com.example.util.CodeCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author special.fy
 */
@Service
public class UserService {

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

            userRepository.save(user);
            return RestResult.successResult(null, String.format("Create user %s success.", user.getUsername()));
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public RestResult<IDToken> signin(UserDTO userDTO) {
        if (!userRepository.existsByUsername(userDTO.getUsername())){
            return RestResult.failResult(HttpStatus.BAD_REQUEST.value(), "username does not exist.");
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        String inputPassword = CodeCUtil.encodePassword(userDTO.getPassword(), config.getSalt());
        if (!user.getPassword().equals(inputPassword)) {
            return RestResult.failResult(HttpStatus.BAD_REQUEST.value(), "password is wrong.");
        }

        String token = jwtTokenProvider.createToken(userDTO.getUsername());

        return RestResult.successResult(new IDToken(token));
    }
}
