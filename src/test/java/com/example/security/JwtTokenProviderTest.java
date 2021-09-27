package com.example.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author special.fy
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void validToken() {
        String username = "mini-authz-server";
        String token = jwtTokenProvider.createToken(username);
        System.out.println(token);

        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }
}
