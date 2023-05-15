package com.paperized.worldscrape.repository;

import com.paperized.worldscrape.entity.ScraperFileConfiguration;
import com.paperized.worldscrape.entity.ScraperFileParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScraperFileConfigurationRepository extends JpaRepository<ScraperFileConfiguration, Long> {
}
