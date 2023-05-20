package com.paperized.worldscrape.repository;

import com.paperized.worldscrape.entity.ScraperFileConfiguration;
import com.paperized.worldscrape.entity.ScraperFileParameter;
import com.paperized.worldscrape.entity.utils.ScraperConfigPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ScraperFileConfigurationRepository extends JpaRepository<ScraperFileConfiguration, Long> {
  boolean existsByIdAndCreatedBy_Id(Long id, Long createdBy);
  Optional<ScraperFileConfiguration> findByIdAndCreatedBy_Id(Long id, Long createdBy);
  List<ScraperFileConfiguration> findAllByCreatedBy_IdOrPolicy(Long createdBy, ScraperConfigPolicy policy);
  List<ScraperFileConfiguration> findAllByPolicy(ScraperConfigPolicy policy);
}
