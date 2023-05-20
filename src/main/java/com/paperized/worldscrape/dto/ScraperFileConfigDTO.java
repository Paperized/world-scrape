package com.paperized.worldscrape.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paperized.worldscrape.dto.utils.Dto;
import com.paperized.worldscrape.entity.ScraperFileConfiguration;
import com.paperized.worldscrape.entity.User;
import com.paperized.worldscrape.entity.utils.ScraperConfigPolicy;
import com.paperized.worldscrape.entity.utils.ScraperFileParamType;
import com.paperized.worldscrape.util.MapperUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScraperFileConfigDTO implements Dto<ScraperFileConfiguration> {
  private Long id;
  private String name;
  private String description;
  private String urlStyle;
  private String configurationUrl;
  private String policy;
  private List<ScraperFileParamDTO> params;
  private Long userId;
  private String userUsername;

  public static ScraperFileConfigDTO fullFileConfig(ScraperFileConfiguration fileParameter) {
    List<ScraperFileParamDTO> paramDTOS = fileParameter.getFileParameters().stream()
      .map(x -> MapperUtil.mapTo(x, ScraperFileParamDTO::paramWithoutFileId))
      .collect(Collectors.toList());

    return new ScraperFileConfigDTO(
      fileParameter.getId(),
      fileParameter.getName(),
      fileParameter.getDescription(),
      fileParameter.getUrlStyle(),
      fileParameter.getConfigurationUrl(),
      fileParameter.getPolicy().name(),
      paramDTOS,
      fileParameter.getCreatedBy().getId(),
      fileParameter.getCreatedBy().getUsername()
    );
  }

  @Override
  public ScraperFileConfiguration toEntity() {
    User user = new User();
    user.setId(userId);
    return new ScraperFileConfiguration(
      id,
      name,
      description,
      urlStyle,
      configurationUrl,
      ScraperConfigPolicy.valueOf(policy == null ? "PRIVATE" : policy),
      params.stream().map(ScraperFileParamDTO::toEntity).collect(Collectors.toList()),
      user
    );
  }
}
