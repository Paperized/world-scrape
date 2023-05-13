package com.paperized.worldscrape.service;

import java.util.Map;

public interface ScraperService {
  Map<String, Object> requestScraping(Map<String, Object> scrapeParameters);
}
