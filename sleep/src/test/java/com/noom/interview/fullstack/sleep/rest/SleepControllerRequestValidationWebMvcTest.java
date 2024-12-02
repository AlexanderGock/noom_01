package com.noom.interview.fullstack.sleep.rest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.noom.interview.fullstack.sleep.db.entity.UserEntity;
import com.noom.interview.fullstack.sleep.db.mapper.UserEntityToUserDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.UserRepository;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDomainToSleepResourceMapper;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDtoToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.service.ISleepManagementService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(SleepController.class)
class SleepControllerRequestValidationWebMvcTest extends AbstractSleepControllerWebMvcTest {

  private static final String BASE_URL = "/api/sleeps";

  private static final String REQUEST_PATTERN = "{\n"
      + "    \"sleepFrom\": \"%s\",\n"
      + "    \"sleepTo\": \"%s\",\n"
      + "    \"mood\": \"%s\"\n"
      + "}";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ISleepManagementService sleepManagementService;

  @MockBean
  private SleepDtoToSleepDomainMapper sleepDtoToSleepDomainMapper;

  @MockBean
  private SleepDomainToSleepResourceMapper sleepDomainToSleepResourceMapper;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserEntityToUserDomainMapper userEntityToUserDomainMapper;

  @Test
  void shouldFailPostingSleepWithoutSleepFrom() throws Exception {
    // given
    String requestBody = buildRequestBodyWithoutSleepFrom();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(createPostRequest(BASE_URL, requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepFrom: must not be null"));
  }

  @Test
  void shouldFailPostingSleepWithoutSleepTo() throws Exception {
    // given
    String requestBody = buildRequestBodyWithoutSleepTo();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(createPostRequest(BASE_URL, requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepTo: must not be null"));
  }

  @Test
  void shouldFailPostingSleepWithoutMood() throws Exception {
    // given
    String requestBody = buildRequestBodyWithoutMood();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(createPostRequest(BASE_URL, requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("mood: must not be null"));
  }

  @Test
  void shouldFailPostingSleepWithCorruptedTime() throws Exception {
    // given
    String requestBody = String.format(REQUEST_PATTERN, "23:00:00", "07:00:0000", Mood.GOOD.name());
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(createPostRequest(BASE_URL, requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepTo: 07:00:0000"));
  }

  @Test
  void shouldFailPostingSleepWithInvalidTime() throws Exception {
    // given
    String requestBody = String.format(REQUEST_PATTERN, "25:01:01", "07:00", Mood.GOOD.name());
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(createPostRequest(BASE_URL, requestBody));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.error").value("sleepFrom: 25:01:01"));
  }
}
