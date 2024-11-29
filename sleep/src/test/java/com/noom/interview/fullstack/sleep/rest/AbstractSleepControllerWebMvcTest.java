package com.noom.interview.fullstack.sleep.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

  protected MockHttpServletRequestBuilder createPostRequest(String url, String requestBody) {
    return MockMvcRequestBuilders.post(url).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody).header("user-id", "100");
  }

  protected MockHttpServletRequestBuilder createGetRequest(String url) {
    return MockMvcRequestBuilders.get(url).contentType(
        MediaType.APPLICATION_JSON_VALUE).header("user-id", "100");
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
