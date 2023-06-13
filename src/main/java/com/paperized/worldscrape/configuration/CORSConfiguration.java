package com.paperized.worldscrape.configuration;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfiguration {
  Logger logger = LoggerFactory.getLogger(CORSConfiguration.class);
  @Value("${ws.frontend.url}")
  private String wsFrontendUrl;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        logger.info("[CONFIG] cors: {}", wsFrontendUrl);
        registry.addMapping("/**").allowedMethods("GET","POST","PUT","DELETE").allowedOrigins(wsFrontendUrl);
      }
    };
  }
}
