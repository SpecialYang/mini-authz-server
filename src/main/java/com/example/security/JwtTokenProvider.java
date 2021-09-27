package com.example.security;

import com.example.exception.CustomException;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.rsa-jwk}")
    private String rsaJWK;

    @Value("${security.jwt.token.expire-minute:60}")
    private long expiry; // 1h

    @Value("${security.jwt.token.kid:kid}")
    private String kid;

    @Value("${security.jwt.token.issuer:issuer}")
    private String issuer;

    private RsaJsonWebKey rsaJWKInstance;

    @PostConstruct
    public void initPrivateKey() throws JoseException {
        rsaJWKInstance = new RsaJsonWebKey(JsonUtil.parseJson(rsaJWK));
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
            jws.setKey(rsaJWKInstance.getPrivateKey());

            return jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
            throw new CustomException("Create token fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean validateToken(String token) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(rsaJWKInstance.getPublicKey())
                    .setSkipDefaultAudienceValidation()
                    .build();
            jwtConsumer.processToClaims(token);
            return true;
        } catch (InvalidJwtException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
