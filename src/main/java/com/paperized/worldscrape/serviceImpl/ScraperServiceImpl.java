package com.paperized.worldscrape.serviceImpl;

import com.paperized.worldscrape.dto.ScraperFileConfigDTO;
import com.paperized.worldscrape.entity.ScraperFileConfiguration;
import com.paperized.worldscrape.exception.ScraperRequestFailedException;
import com.paperized.worldscrape.repository.ScraperFileConfigurationRepository;
import com.paperized.worldscrape.service.ScraperService;
import com.paperized.worldscrape.util.MapperUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScraperServiceImpl implements ScraperService {
  private final ScraperFileConfigurationRepository scraperFileConfigurationRepository;
  private final RestTemplate restTemplate;

  public ScraperServiceImpl(ScraperFileConfigurationRepository scraperFileConfigurationRepository, RestTemplateBuilder restTemplateBuilder) {
    this.scraperFileConfigurationRepository = scraperFileConfigurationRepository;
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

  @Override
  public List<ScraperFileConfigDTO> getAllFileConfig(Function<ScraperFileConfiguration, ScraperFileConfigDTO> mapFn) {
    return scraperFileConfigurationRepository.findAll().stream().map(x -> MapperUtil.mapTo(x, mapFn)).collect(Collectors.toList());
  }

  @Override
  public ScraperFileConfigDTO createFileConfig(ScraperFileConfigDTO dto, Function<ScraperFileConfiguration, ScraperFileConfigDTO> mapFn) {
    ScraperFileConfiguration fileConfiguration = dto.toEntity();
    fileConfiguration.setId(null);
    fileConfiguration.fileParameters.forEach(x -> x.setFileConfiguration(null));
    return MapperUtil.mapTo(scraperFileConfigurationRepository.save(fileConfiguration), mapFn);
  }
}
