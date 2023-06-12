package com.paperized.worldscrape.serviceImpl;

import com.paperized.worldscrape.controller.ScraperController;
import com.paperized.worldscrape.dto.ScraperFileConfigDTO;
import com.paperized.worldscrape.entity.ScraperFileConfiguration;
import com.paperized.worldscrape.entity.User;
import com.paperized.worldscrape.entity.utils.ScraperConfigPolicy;
import com.paperized.worldscrape.exception.ScraperRequestFailedException;
import com.paperized.worldscrape.repository.ScraperFileConfigurationRepository;
import com.paperized.worldscrape.security.util.AuthenticatedUser;
import com.paperized.worldscrape.security.util.SecurityUtils;
import com.paperized.worldscrape.service.ScraperService;
import com.paperized.worldscrape.util.MapperUtil;
import org.springframework.beans.factory.annotation.Value;
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

import static com.paperized.worldscrape.security.AuthRole.SIMPLE_ROLE_ADMIN;

@Service
public class ScraperServiceImpl implements ScraperService {
  private final ScraperFileConfigurationRepository scraperFileConfigurationRepository;
  private final RestTemplate restTemplate;
  private final AmazonClient amazonClient;
  private final TransactionTemplate transactionTemplate;

  @Value("${ws.scrapaper.url}")
  private String wsScrapaperUrl;

  public ScraperServiceImpl(ScraperFileConfigurationRepository scraperFileConfigurationRepository, RestTemplateBuilder restTemplateBuilder, AmazonClient amazonClient, TransactionTemplate transactionTemplate) {
    this.scraperFileConfigurationRepository = scraperFileConfigurationRepository;
    this.restTemplate = restTemplateBuilder.build();
    this.amazonClient = amazonClient;
    this.transactionTemplate = transactionTemplate;
  }

  @Override
  public Map<String, Object> requestScraping(Map<String, Object> scrapeParameters) {
    try {
      ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(wsScrapaperUrl.concat("/scrape"), HttpMethod.POST,
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
    AuthenticatedUser authenticatedUser = SecurityUtils.getCurrentUserOrNull();
    List<ScraperFileConfiguration> scraperFileConfigurations;
    if(authenticatedUser == null) {
      scraperFileConfigurations = scraperFileConfigurationRepository.findAllByPolicy(ScraperConfigPolicy.PUBLIC);
    } else if(authenticatedUser.getAuthorities().contains(SIMPLE_ROLE_ADMIN)) {
      scraperFileConfigurations = scraperFileConfigurationRepository.findAll();
    } else {
      scraperFileConfigurations = scraperFileConfigurationRepository.findAllByCreatedBy_IdOrPolicy(authenticatedUser.getId(), ScraperConfigPolicy.PUBLIC);
    }

    return scraperFileConfigurations.stream().map(x -> MapperUtil.mapTo(x, mapFn)).collect(Collectors.toList());
  }

  @Override
  public ScraperFileConfigDTO createOrUpdateFileConfig(ScraperController.CreateOrUpdateScraperDTO dto, Function<ScraperFileConfiguration, ScraperFileConfigDTO> mapFn) {
    assertConfigurationText(dto.configText());

    final ScraperFileConfiguration fileConfiguration = dto.configuration().toEntity();
    boolean isNew = fileConfiguration.getId() == null;
    String configUrl = "";

    AuthenticatedUser authenticatedUser = SecurityUtils.getCurrentUser();

    if (!isNew) {
      configUrl = String.format("%s/%d.yaml", amazonClient.getStoragePath(), fileConfiguration.getId());
      fileConfiguration.fileParameters.forEach(x -> x.setFileConfiguration(fileConfiguration));
      if(!authenticatedUser.getAuthorities().contains(SIMPLE_ROLE_ADMIN)) {
        if(scraperFileConfigurationRepository.existsByIdAndCreatedBy_Id(fileConfiguration.getId(), authenticatedUser.getId())) {
          fileConfiguration.getCreatedBy().setId(authenticatedUser.getId());
        } else {
          throw new IllegalArgumentException("You are not allowed to update this configuration");
        }
      }
    } else {
      fileConfiguration.setCreatedBy(new User(authenticatedUser.getId()));
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
    boolean deleted = Boolean.TRUE.equals(transactionTemplate.execute(status -> _deleteFileConfigTx(id)));
    if (deleted)
      amazonClient.removeFile(id + ".yaml");
  }

  private boolean _deleteFileConfigTx(Long id) {
    AuthenticatedUser authenticatedUser = SecurityUtils.getCurrentUser();
    boolean exists;
    if(authenticatedUser.getAuthorities().contains(SIMPLE_ROLE_ADMIN)) {
      exists = this.scraperFileConfigurationRepository.existsById(id);
    } else {
      exists = this.scraperFileConfigurationRepository.existsByIdAndCreatedBy_Id(id, authenticatedUser.getId());
      if(!exists) {
        throw new IllegalArgumentException("You are not allowed to delete this configuration");
      }
    }

    if(exists) {
      this.scraperFileConfigurationRepository.deleteById(id);
    }

    return exists;
  }

  private void assertConfigurationText(String configText) {
    try {
      ResponseEntity<ResponseConfigValidation> responseEntity = restTemplate.postForEntity(wsScrapaperUrl.concat("/check-config"),
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
