package org.goorm.goormthon.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:8081", "http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "https://front-end-alpha-drab.vercel.app")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3000);
    }
}