package com.example.security;

import lombok.SneakyThrows;
import org.jose4j.jwt.JwtClaims;
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

    @SneakyThrows
    @Test
    public void validToken() {
        String username = "mini-authz-server";
        String[] apis = {"/user", "/order"};
        String token = jwtTokenProvider.createToken(username);
        System.out.println(token);

        JwtClaims claims = jwtTokenProvider.validateToken(token);
        Assert.assertEquals(username, claims.getClaimValue("username"));
    }
}
