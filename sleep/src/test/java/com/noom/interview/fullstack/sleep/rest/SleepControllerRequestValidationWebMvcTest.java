package com.noom.interview.fullstack.sleep.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDomainToSleepResourceMapper;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDtoToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import com.noom.interview.fullstack.sleep.service.ISleepManagementService;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(SleepController.class)
class SleepControllerRequestValidationWebMvcTest {

  private static final String BASE_URL = "/api/sleeps";

  private static final String REQUEST_PATTERN = "{\n"
      + "    \"sleepFrom\": \"%s\",\n"
      + "    \"sleepTo\": \"%s\",\n"
      + "    \"mood\": \"%s\"\n"
      + "}";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ISleepManagementService sleepManagementService;

  @MockBean
  private SleepDtoToSleepDomainMapper sleepDtoToSleepDomainMapper;

  @MockBean
  private SleepDomainToSleepResourceMapper sleepDomainToSleepResourceMapper;

  @Test
  void shouldFailPostingSleepWithoutSleepFrom() throws Exception {
    // given
    String requestBody = buildRequestBodyWithoutSleepFrom();

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepFrom: must not be null"));
  }

  @Test
  void shouldFailPostingSleepWithoutSleepTo() throws Exception {
    // given
    String requestBody = buildRequestBodyWithoutSleepTo();

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepTo: must not be null"));
  }

  @Test
  void shouldFailPostingSleepWithoutMood() throws Exception {
    // given
    String requestBody = buildRequestBodyWithoutMood();

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("mood: must not be null"));
  }

  @Test
  void shouldFailPostingSleepWithCorruptedTime() throws Exception {
    // given
    String requestBody = String.format(REQUEST_PATTERN, "23:00:00", "07:00:0000", Mood.GOOD.name());

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepTo: 07:00:0000"));
  }

  @Test
  void shouldFailPostingSleepWithInvalidTime() throws Exception {
    // given
    String requestBody = String.format(REQUEST_PATTERN, "25:01:01", "07:00", Mood.GOOD.name());

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepFrom: 25:01:01"));
  }

  @Test
  void shouldSucceedPostingSleep() throws Exception {
    // given
    String requestBody = buildRequestBody(true, true, true);

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void shouldSucceedGettingLastNight() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/lastnight").contentType(
        MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.error").doesNotExist());
  }

  private String buildRequestBodyWithoutSleepFrom() throws JsonProcessingException {
    return buildRequestBody(false, true, true);
  }

  private String buildRequestBodyWithoutSleepTo() throws JsonProcessingException {
    return buildRequestBody(true, false, true);
  }

  private String buildRequestBodyWithoutMood() throws JsonProcessingException {
    return buildRequestBody(true, true, false);
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
