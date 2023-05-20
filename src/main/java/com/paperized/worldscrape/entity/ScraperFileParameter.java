package com.paperized.worldscrape.entity;

import com.paperized.worldscrape.entity.utils.ScraperFileParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scraper_file_parameters")
public class ScraperFileParameter {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ScraperFileParamType fileParamType;

  private String defaultValue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fileConfiguration_id", nullable = false)
  private ScraperFileConfiguration fileConfiguration;

  public ScraperFileParameter(Long id) {
    this.id = id;
  }
}
