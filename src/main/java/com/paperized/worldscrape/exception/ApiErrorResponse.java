package com.paperized.worldscrape.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class ApiErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    private HttpStatus status;
    private List<ApiError> errors;

    public static ApiErrorResponse fromErrors(HttpStatus status, List<ApiError> errors) {
        return new ApiErrorResponse(LocalDateTime.now(), status, errors);
    }

    public static ApiErrorResponse fromErrors(HttpStatus status, ApiError... errors) {
        return new ApiErrorResponse(LocalDateTime.now(), status, List.of(errors));
    }

  public static ApiErrorResponse fromErrors(HttpStatus status, String errorCode, String errorMessage) {
    return new ApiErrorResponse(LocalDateTime.now(), status, List.of(new ApiError(errorCode, errorMessage)));
  }
}
