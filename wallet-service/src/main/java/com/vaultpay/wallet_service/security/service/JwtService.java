package com.vaultpay.wallet_service.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.flywaydb.core.internal.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JwtService {
    private final PublicKey publicKey;

    public JwtService(PublicKey publicKey){
        this.publicKey = publicKey;
    }

    public Claims getAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <G> G getClaim(String token, String claimName, Class<G> type){
        Claims claims = this.getAllClaims(token);

        return claims.get(claimName, type);
    }

    public boolean verifyToken(String token){
        try{
            Claims claims = this.getAllClaims(token);

            return !claims.getExpiration().before(new Date());
        }catch (JwtException jwtException){
            return false;
        }
    }
}
