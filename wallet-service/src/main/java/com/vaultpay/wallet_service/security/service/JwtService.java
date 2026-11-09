package com.vaultpay.wallet_service.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import java.security.PublicKey;
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

            if (claims.getExpiration()==null){
                return false;
            }

            return !claims.getExpiration().before(new Date());
        }catch (JwtException jwtException){
            return false;
        }
    }
}
