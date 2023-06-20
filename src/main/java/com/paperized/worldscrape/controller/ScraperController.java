package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.dto.ScraperFileConfigDTO;
import com.paperized.worldscrape.security.util.IsAuthenticated;
import com.paperized.worldscrape.service.ScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scraper")
public class ScraperController {
  Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final ScraperService scraperService;

  public ScraperController(ScraperService scraperService) {
    this.scraperService = scraperService;
  }

  @GetMapping("/file-configs/all")
  public List<ScraperFileConfigDTO> getAllFileConfig() {
    logger.info("[REQ] getAllFileConfig");
    return scraperService.getAllFileConfig(ScraperFileConfigDTO::fullFileConfig);
  }

  @IsAuthenticated
  @PostMapping("/file-configs/create-or-update")
  public ScraperFileConfigDTO createOrUpdateFileConfig(@RequestBody CreateOrUpdateScraperDTO scraperFileConfigDTO) {
    logger.info("[REQ] createOrUpdateFileConfig: {}", scraperFileConfigDTO);
    return scraperService.createOrUpdateFileConfig(scraperFileConfigDTO, ScraperFileConfigDTO::fullFileConfig);
  }

  @IsAuthenticated
  @DeleteMapping("/file-configs/{id}")
  public void deleteFileConfig(@PathVariable Long id) {
    logger.info("[REQ] deleteFileConfig: {}", id);
    scraperService.deleteFileConfig(id);
  }

  public record CreateOrUpdateScraperDTO(ScraperFileConfigDTO configuration, String configText) {
  }
}
