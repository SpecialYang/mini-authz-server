package com.example.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author special.fy
 */
@Data
public class UserDTO {

    private String username;

    private String password;

    private List<String> apis;
}
