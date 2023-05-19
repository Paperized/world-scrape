package com.paperized.worldscrape.serviceImpl;

import com.paperized.worldscrape.controller.ScraperController;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScraperServiceImpl implements ScraperService {
  private final ScraperFileConfigurationRepository scraperFileConfigurationRepository;
  private final RestTemplate restTemplate;
  private final AmazonClient amazonClient;
  private final TransactionTemplate transactionTemplate;

  public ScraperServiceImpl(ScraperFileConfigurationRepository scraperFileConfigurationRepository, RestTemplateBuilder restTemplateBuilder, AmazonClient amazonClient, TransactionTemplate transactionTemplate) {
    this.scraperFileConfigurationRepository = scraperFileConfigurationRepository;
    this.restTemplate = restTemplateBuilder.build();
    this.amazonClient = amazonClient;
    this.transactionTemplate = transactionTemplate;
  }

  @Override
  public Map<String, Object> requestScraping(Map<String, Object> scrapeParameters) {
    try {
      ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange("http://localhost:5100/", HttpMethod.POST,
        new HttpEntity<>(scrapeParameters), new ParameterizedTypeReference<>() {
        });
      return responseEntity.getBody();
    } catch (RestClientException exception) {
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
  public ScraperFileConfigDTO createOrUpdateFileConfig(ScraperController.CreateOrUpdateScraperDTO dto, Function<ScraperFileConfiguration, ScraperFileConfigDTO> mapFn) {
    assertConfigurationText(dto.configText());

    final ScraperFileConfiguration fileConfiguration = dto.configuration().toEntity();
    boolean isNew = fileConfiguration.getId() == null;
    String configUrl = "";

    if (!isNew) {
      configUrl = String.format("%s/%d.yaml", amazonClient.getStoragePath(), fileConfiguration.getId());
      fileConfiguration.fileParameters.forEach(x -> x.setFileConfiguration(fileConfiguration));
    } else {
      fileConfiguration.fileParameters.forEach(x -> {
        x.setId(null);
        x.setFileConfiguration(fileConfiguration);
      });
    }

    fileConfiguration.setConfigurationUrl(configUrl);

    ScraperFileConfiguration outputConfiguration = scraperFileConfigurationRepository.saveAndFlush(fileConfiguration);
    if(isNew) {
      configUrl = String.format("%s/%d.yaml", amazonClient.getStoragePath(), fileConfiguration.getId());
      outputConfiguration.setConfigurationUrl(configUrl);
      outputConfiguration = scraperFileConfigurationRepository.save(fileConfiguration);
    }

    try {
      amazonClient.uploadFile(outputConfiguration.getId().toString() + ".yaml", dto.configText());
    } catch (Exception ex) {
      scraperFileConfigurationRepository.delete(outputConfiguration);
      throw ex;
    }

    return MapperUtil.mapTo(outputConfiguration, mapFn);
  }

  @Override
  public void deleteFileConfig(Long id) {
    this.transactionTemplate.setReadOnly(false);
    ScraperFileConfiguration deletedConfig = transactionTemplate.execute(status -> _deleteFileConfigTx(id));
    if (deletedConfig != null)
      amazonClient.removeFile(deletedConfig.getConfigurationUrl());
  }

  private ScraperFileConfiguration _deleteFileConfigTx(Long id) {
    Optional<ScraperFileConfiguration> fileConfigurationOptional = this.scraperFileConfigurationRepository.findById(id);
    if(fileConfigurationOptional.isPresent()) {
      ScraperFileConfiguration fileConfiguration = fileConfigurationOptional.get();
      this.scraperFileConfigurationRepository.deleteById(fileConfiguration.getId());
      return fileConfiguration;
    }

    return null;
  }

  private void assertConfigurationText(String configText) {
    try {
      ResponseEntity<ResponseConfigValidation> responseEntity = restTemplate.postForEntity("http://localhost:5100/check-config",
        new RequestConfigValidation(configText), ResponseConfigValidation.class);
      String errorMessage = responseEntity.getHeaders().getFirst("error-message");
      if (errorMessage != null)
        throw new ScraperRequestFailedException(responseEntity.getStatusCode(), errorMessage);
    } catch (RestClientException exception) {
      if (!(exception instanceof HttpClientErrorException ex))
        throw exception;

      String errorMessage = ex.getResponseHeaders() != null ?
        ex.getResponseHeaders().getFirst("error-message") :
        "No error message provided...";
      throw new ScraperRequestFailedException(ex.getStatusCode(), errorMessage);
    }
  }

  record ResponseConfigValidation(boolean isValid) {
  }

  record RequestConfigValidation(String configText) {
  }
}
