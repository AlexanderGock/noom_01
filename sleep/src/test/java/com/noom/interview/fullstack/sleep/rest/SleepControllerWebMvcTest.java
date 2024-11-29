package com.noom.interview.fullstack.sleep.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.exception.RecordAlreadyExistsException;
import com.noom.interview.fullstack.sleep.exception.SleepNotFoundException;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDomainToSleepResourceMapper;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDtoToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.rest.response.SleepResource;
import com.noom.interview.fullstack.sleep.service.ISleepManagementService;
import java.time.LocalDate;
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
class SleepControllerWebMvcTest extends AbstractSleepControllerWebMvcTest {

  private static final String BASE_URL = "/api/sleeps";

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
  void shouldSucceedPostingSleep() throws Exception {
    // given
    String requestBody = buildValidRequestBody();
    SleepResource sleepResource = SleepResource.builder()
        .sleepFrom(LocalTime.parse("23:00:00"))
        .sleepTo(LocalTime.parse("06:30:00"))
        .sleepDay(LocalDate.parse("2024-11-29"))
        .duration(LocalTime.parse("07:30:00"))
        .mood(Mood.OK)
        .build();
    given(sleepDomainToSleepResourceMapper.mapToSleepResource(any())).willReturn(sleepResource);

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.error").doesNotExist());
    resultActions.andExpect(jsonPath("$.sleepFrom").value("23:00:00"));
    resultActions.andExpect(jsonPath("$.sleepTo").value("06:30:00"));
    resultActions.andExpect(jsonPath("$.sleepDay").value("2024-11-29"));
    resultActions.andExpect(jsonPath("$.duration").value("07:30:00"));
    resultActions.andExpect(jsonPath("$.mood").value("OK"));
  }

  @Test
  void shouldReplyWith400WhenPostingDuplicate() throws Exception {
    // given
    String requestBody = buildValidRequestBody();
    given(sleepManagementService.createSleep(any(), any())).willThrow(
        RecordAlreadyExistsException.class);

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").hasJsonPath());
  }

  @Test
  void shouldReplyWithCode500WhenCreateSleepCausingException() throws Exception {
    // given
    String requestBody = buildValidRequestBody();
    given(sleepManagementService.createSleep(any(), any())).willThrow(RuntimeException.class);

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isInternalServerError());
    resultActions.andExpect(jsonPath("$.error").hasJsonPath());
  }

  @Test
  void shouldSucceedGettingLastNight() throws Exception {
    // given
    SleepResource sleepResource = SleepResource.builder()
        .sleepFrom(LocalTime.parse("23:00:00"))
        .sleepTo(LocalTime.parse("06:30:00"))
        .sleepDay(LocalDate.parse("2024-11-29"))
        .duration(LocalTime.parse("07:30:00"))
        .mood(Mood.OK)
        .build();
    given(sleepDomainToSleepResourceMapper.mapToSleepResource(any())).willReturn(sleepResource);

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL + "/lastnight").contentType(
            MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.error").doesNotExist());
    resultActions.andExpect(jsonPath("$.sleepFrom").value("23:00:00"));
    resultActions.andExpect(jsonPath("$.sleepTo").value("06:30:00"));
    resultActions.andExpect(jsonPath("$.sleepDay").value("2024-11-29"));
    resultActions.andExpect(jsonPath("$.duration").value("07:30:00"));
    resultActions.andExpect(jsonPath("$.mood").value("OK"));
  }

  @Test
  void shouldReplyWith404WhenNoLastNightSleepFound() throws Exception {
    // given
    given(sleepManagementService.getLastNightSleep(any())).willThrow(SleepNotFoundException.class);

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL + "/lastnight").contentType(
            MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions.andExpect(status().isNotFound());
    resultActions.andExpect(jsonPath("$.error").hasJsonPath());
  }

  @Test
  void shouldReplyWithCode500WhenGetLastNightSleepCausingException() throws Exception {
    // given
    given(sleepManagementService.getLastNightSleep(any())).willThrow(RuntimeException.class);

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL + "/lastnight").contentType(
            MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions.andExpect(status().isInternalServerError());
    resultActions.andExpect(jsonPath("$.error").hasJsonPath());
  }
}
