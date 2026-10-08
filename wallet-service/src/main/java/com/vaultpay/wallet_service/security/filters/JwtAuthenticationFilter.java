package com.vaultpay.wallet_service.security.filters;

import com.vaultpay.wallet_service.security.principal.WalletUserPrincipal;
import com.vaultpay.wallet_service.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

       Cookie[] cookies =  request.getCookies();

       if (cookies==null){
           filterChain.doFilter(request,response);
           return;
       }

       Cookie accessCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("access_token")).findFirst().orElse(null);

       if (accessCookie==null){
           filterChain.doFilter(request, response);
           return;
       }

        String accessToken = accessCookie.getValue();

       if (!jwtService.verifyToken(accessToken)){
           filterChain.doFilter(request, response);
           return;
       }

       String userId = jwtService.getClaim(accessToken, "sub", String.class);

       WalletUserPrincipal walletUserPrincipal = new WalletUserPrincipal(userId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                walletUserPrincipal,
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }


}
