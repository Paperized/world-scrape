package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.dto.ScraperFileConfigDTO;
import com.paperized.worldscrape.security.util.AuthenticatedUser;
import com.paperized.worldscrape.security.util.IsAuthenticated;
import com.paperized.worldscrape.service.ScraperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scraper")
public class ScraperController {
  private final ScraperService scraperService;

  public ScraperController(ScraperService scraperService) {
    this.scraperService = scraperService;
  }

  @PostMapping("/run")
  public Map<String, Object> runScraper(@RequestBody Map<String, Object> scraperParams) {
    return scraperService.requestScraping(scraperParams);
  }

  @GetMapping("/file-configs/all")
  public List<ScraperFileConfigDTO> getAllFileConfig() {
    return scraperService.getAllFileConfig(ScraperFileConfigDTO::fullFileConfig);
  }

  @IsAuthenticated
  @PostMapping("/file-configs/create-or-update")
  public ScraperFileConfigDTO createOrUpdateFileConfig(@RequestBody CreateOrUpdateScraperDTO scraperFileConfigDTO) {
    return scraperService.createOrUpdateFileConfig(scraperFileConfigDTO, ScraperFileConfigDTO::fullFileConfig);
  }

  @IsAuthenticated
  @DeleteMapping("/file-configs/{id}")
  public void deleteFileConfig(@PathVariable Long id) {
    scraperService.deleteFileConfig(id);
  }

  public record CreateOrUpdateScraperDTO(ScraperFileConfigDTO configuration, String configText) {}
}
