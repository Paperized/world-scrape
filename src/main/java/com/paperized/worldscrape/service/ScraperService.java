package com.paperized.worldscrape.service;

import com.paperized.worldscrape.controller.ScraperController;
import com.paperized.worldscrape.dto.ScraperFileConfigDTO;
import com.paperized.worldscrape.entity.ScraperFileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ScraperService {
  List<ScraperFileConfigDTO> getAllFileConfig(Function<ScraperFileConfiguration, ScraperFileConfigDTO> mapFn);
  ScraperFileConfigDTO createOrUpdateFileConfig(ScraperController.CreateOrUpdateScraperDTO dto, Function<ScraperFileConfiguration, ScraperFileConfigDTO> mapFn);
  void deleteFileConfig(Long id);
}
