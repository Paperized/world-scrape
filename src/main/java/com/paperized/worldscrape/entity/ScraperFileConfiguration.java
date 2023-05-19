package com.paperized.worldscrape.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scraper_file_configurations")
public class ScraperFileConfiguration {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;
  @Column(nullable = false)
  private String urlStyle;

  @Column(nullable = false)
  private String configurationUrl;

  @OneToMany(mappedBy = "fileConfiguration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  public List<ScraperFileParameter> fileParameters = new ArrayList<>();
}
