package com.vaultpay.wallet_service.security.config;

import com.vaultpay.wallet_service.security.filters.JwtAuthenticationFilter;
import com.vaultpay.wallet_service.wallet.entity.Wallet;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WalletSecurityConfig {
    @Value("jwt.public-key")
    private String publicKeyPath;
    private final ResourceLoader resourceLoader;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public WalletSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ResourceLoader resourceLoader){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.resourceLoader = resourceLoader;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.
                sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request->
                request.anyRequest().permitAll()
        ).formLogin(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public PublicKey publicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Resource resource = resourceLoader.getResource(publicKeyPath);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        resource.getInputStream(),
                        StandardCharsets.UTF_8
                ))) {

            String fileContent = reader.lines()
                    .collect(Collectors.joining("\n"));

            fileContent = fileContent
                    .replaceAll(
                            "-----BEGIN PUBLIC KEY-----|-----END PUBLIC KEY-----",
                            ""
                    )
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(fileContent);

            return KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
        }
    }
}
