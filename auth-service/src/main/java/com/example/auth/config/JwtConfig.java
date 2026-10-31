package com.example.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public RSAKey rsaKey(
            @Value("${jwt.private-key-location}") Resource privateKeyResource,
            @Value("${jwt.public-key-location}") Resource publicKeyResource) throws Exception {

        String privatePem = new String(
            privateKeyResource.getInputStream().readAllBytes(),
            StandardCharsets.UTF_8
        );

        String publicPem = new String(
            publicKeyResource.getInputStream().readAllBytes(),
            StandardCharsets.UTF_8
        );

        byte[] privateBytes = decodePem(privatePem, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
        byte[] publicBytes = decodePem(publicPem, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));

        return new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID("car-rental-auth-key")
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAKey rsaKey) {
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    private byte[] decodePem(String pem, String header, String footer) {
        String base64 = pem
            .replace(header, "")
            .replace(footer, "")
            .replaceAll("\\s", "");
        return Base64.getDecoder().decode(base64);
    }
}
