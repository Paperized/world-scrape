package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.service.ScraperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
