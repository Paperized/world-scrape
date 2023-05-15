package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.dto.ScraperFileConfigDTO;
import com.paperized.worldscrape.service.ScraperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/scraper")
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

  @PostMapping("/file-configs/create")
  public ScraperFileConfigDTO createFileConfig(@RequestBody ScraperFileConfigDTO scraperFileConfigDTO) {
    return scraperService.createFileConfig(scraperFileConfigDTO, ScraperFileConfigDTO::fullFileConfig);
  }
}
