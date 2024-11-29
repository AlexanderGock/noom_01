package com.noom.interview.fullstack.sleep.rest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

  @Builder.Default
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  LocalDateTime timestamp = LocalDateTime.now();
  int status;
  String error;
  String path;
}
