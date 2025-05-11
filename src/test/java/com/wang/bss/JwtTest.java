package com.wang.bss;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1);
        claims.put("username", "20212035");

        String token = JWT.create()
                .withClaim("teacher", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .sign(Algorithm.HMAC256("wanghexiang"));

        System.out.println(token);
    }

    @Test
    public void testParse() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFjaGVyIjp7InVzZXJJZCI6MSwidXNlcm5hbWUiOiIyMDIxMjAzNSJ9LCJleHAiOjE3NDUyOTYwNDl9.a5S7CP5HYCFcWNkg-YgWnm8lX8BBjZjBvgPDIcAdrNs";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("wanghexiang")).build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        System.out.println(claims.get("teacher"));


    }
}
