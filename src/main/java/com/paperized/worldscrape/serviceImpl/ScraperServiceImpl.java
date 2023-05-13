package com.paperized.worldscrape.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.paperized.worldscrape.exception.ScraperRequestFailedException;
import com.paperized.worldscrape.service.ScraperService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ScraperServiceImpl implements ScraperService {
  private final RestTemplate restTemplate;

  public ScraperServiceImpl(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Override
  public Map<String, Object> requestScraping(Map<String, Object> scrapeParameters) {
    try {
      ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange("http://localhost:5100/", HttpMethod.POST,
        new HttpEntity<>(scrapeParameters), new ParameterizedTypeReference<>() {});
      return responseEntity.getBody();
    } catch(RestClientException exception) {
      if (!(exception instanceof HttpClientErrorException ex))
        throw exception;

      String errorMessage = ex.getResponseHeaders() != null ?
                              ex.getResponseHeaders().getFirst("error-message") :
                              "No error message provided...";
      throw new ScraperRequestFailedException(ex.getStatusCode(), errorMessage);
    }
  }
}
