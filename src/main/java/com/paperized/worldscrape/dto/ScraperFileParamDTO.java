package com.paperized.worldscrape.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paperized.worldscrape.dto.utils.Dto;
import com.paperized.worldscrape.entity.ScraperFileConfiguration;
import com.paperized.worldscrape.entity.ScraperFileParameter;
import com.paperized.worldscrape.entity.utils.ScraperFileParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.text.ParseException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScraperFileParamDTO implements Dto<ScraperFileParameter> {
  private Long id;
  private String name;
  private ScraperFileParamType type;
  private Object value;
  private Long fileConfigId;

  public static ScraperFileParamDTO paramWithoutFileId(ScraperFileParameter fileParameter) {
    return new ScraperFileParamDTO(
      fileParameter.getId(),
      fileParameter.getName(),
      fileParameter.getFileParamType(),
      convertValue(fileParameter.getFileParamType(), fileParameter.getDefaultValue()),
      null
    );
  }

  @Override
  public ScraperFileParameter toEntity() {
    ScraperFileConfiguration fileConfig = new ScraperFileConfiguration();
    fileConfig.setId(fileConfigId);
    Object defaultValue = null;
    try {
      defaultValue = convertValue(type, value.toString());
    } catch (Exception ex) {
      throw new RuntimeException("Type and Value dont match");
    }
    return new ScraperFileParameter(id, name, type, defaultValue.toString(), fileConfig);
  }

  private static Object convertValue(ScraperFileParamType type, String value) {
    return switch (type) {
      case bool -> Boolean.getBoolean(value);
      case text -> value;
      case number -> {
        try {
          yield NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
          throw new RuntimeException(e.toString());
        }
      }
      default -> throw new RuntimeException("Unreachable");
    };
  }
}
