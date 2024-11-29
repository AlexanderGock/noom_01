package com.noom.interview.fullstack.sleep.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractSleepControllerWebMvcTest {

  @Autowired
  protected ObjectMapper objectMapper;

  protected String buildRequestBodyWithoutSleepFrom() throws JsonProcessingException {
    return buildRequestBody(false, true, true);
  }

  protected String buildRequestBodyWithoutSleepTo() throws JsonProcessingException {
    return buildRequestBody(true, false, true);
  }

  protected String buildRequestBodyWithoutMood() throws JsonProcessingException {
    return buildRequestBody(true, true, false);
  }

  protected String buildValidRequestBody() throws JsonProcessingException {
    return buildRequestBody(true, true, true);
  }

  private String buildRequestBody(boolean withSleepFrom, boolean withSleepTo, boolean withMood)
      throws JsonProcessingException {
    SleepDto sleepDto = SleepDto.builder()
        .sleepFrom(withSleepFrom ? LocalTime.now() : null)
        .sleepTo(withSleepTo ? LocalTime.now() : null)
        .mood(withMood ? Mood.OK : null)
        .build();
    return objectMapper.writeValueAsString(sleepDto);
  }
}
