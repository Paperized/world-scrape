package com.paperized.worldscrape.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PrefixApiConfiguration implements WebMvcConfigurer {
  Logger logger = LoggerFactory.getLogger(PrefixApiConfiguration.class);
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    logger.info("[CONFIG] configurePathMatch: every controller now starts with /api");
    WebMvcConfigurer.super.configurePathMatch(configurer);
    configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
  }
}
