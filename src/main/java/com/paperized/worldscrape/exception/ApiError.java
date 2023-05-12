package com.paperized.worldscrape.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
  private String errorCode;
  private String errorMessage;
}
