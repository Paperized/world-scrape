package com.paperized.worldscrape.entity;

import com.paperized.worldscrape.entity.utils.ScraperConfigPolicy;
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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ScraperConfigPolicy policy = ScraperConfigPolicy.PRIVATE;

  @OneToMany(mappedBy = "fileConfiguration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  public List<ScraperFileParameter> fileParameters = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by_id")
  private User createdBy;


  public ScraperFileConfiguration(Long id) {
    this.id = id;
  }
}
