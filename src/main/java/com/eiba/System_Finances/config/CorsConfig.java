package com.eiba.System_Finances.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    // Default inclui domínios de produção para funcionar mesmo sem o perfil prod ativo
    @Value("${cors.allowed-origins:http://localhost:5173,http://localhost:3000,https://csc-sistema-front.vercel.app,https://csc-sistema-front-*.vercel.app}")
    private String allowedOriginsRaw;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> patterns = Arrays.asList(allowedOriginsRaw.split(","));

        config.setAllowedOriginPatterns(patterns);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
