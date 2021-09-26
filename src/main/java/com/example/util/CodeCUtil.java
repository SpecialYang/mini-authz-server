package com.example.util;

import org.springframework.util.DigestUtils;

/**
 * @author special.fy
 */
public class CodeCUtil {

    public static String encodePassword(String raw, String salt) {
        return DigestUtils.md5DigestAsHex((raw + salt).getBytes());
    }
}
