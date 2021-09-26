package com.example.security;

import com.example.exception.CustomException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.private-key:private-key}")
    private String privateKey;

    @Value("${security.jwt.token.public-key:public-key}")
    private String publicKey;

    @Value("${security.jwt.token.expire-minute:60}")
    private long expiry; // 1h

    @Value("${security.jwt.token.kid:kid}")
    private String kid;

    @Value("${security.jwt.token.issuer:issuer}")
    private String issuer;

    private PrivateKey key;

    @PostConstruct
    public void initPrivateKey() throws JoseException {
        this.key = new RsaJsonWebKey(JsonUtil.parseJson(this.privateKey)).getPrivateKey();
    }

    public String createToken(String username) {
        try {
            JwtClaims claims = new JwtClaims();
            // iss
            claims.setIssuer(issuer);
            // sub
            claims.setSubject(issuer);
            // aud
            claims.setAudience(username);
            // exp
            claims.setExpirationTimeMinutesInTheFuture(expiry);
            // nbf
            claims.setNotBeforeMinutesInThePast(1);

            JsonWebSignature jws = new JsonWebSignature();
            jws.setKeyIdHeaderValue(kid);
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            jws.setPayload(claims.toJson());
            jws.setKey(key);

            return jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
            throw new CustomException("Create token fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean validateToken(String token) {
        try {
            PublicKey pk = new RsaJsonWebKey(JsonUtil.parseJson(publicKey)).getPublicKey();
            Jwts.parser().setSigningKey(pk).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException | JoseException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
