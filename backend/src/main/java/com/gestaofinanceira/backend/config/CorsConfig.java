package com.gestaofinanceira.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // origens permitido no caso angular 
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // permite os metodos http
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // permite todos os headers
        config.setAllowedHeaders(List.of("*"));

        // Permite cookie, authorization headers
        config.setAllowCredentials(true);

        // tempo pra cachear a resposta em segundos
        config.setMaxAge(3600L);

        // aplica essa configuração a todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
