package com.vaultpay.wallet_service.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Configuration
public class JwtKeyConfig {
    private final ResourceLoader resourceLoader;
    @Value("${jwt.public-key}")
    private String publicKeyPath;
    public JwtKeyConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
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
