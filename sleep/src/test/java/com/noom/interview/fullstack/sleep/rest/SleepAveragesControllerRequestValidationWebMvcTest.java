package com.noom.interview.fullstack.sleep.rest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.noom.interview.fullstack.sleep.db.entity.UserEntity;
import com.noom.interview.fullstack.sleep.db.mapper.UserEntityToUserDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.UserRepository;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepStatisticsDomainToRestMapper;
import com.noom.interview.fullstack.sleep.service.ISleepAveragesService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(SleepAveragesController.class)
class SleepAveragesControllerRequestValidationWebMvcTest {

  private static final String BASE_URL = "/api/sleeps/averages";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ISleepAveragesService sleepAveragesService;

  @MockBean
  private SleepStatisticsDomainToRestMapper sleepStatisticsDomainToRestMapper;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserEntityToUserDomainMapper userEntityToUserDomainMapper;

  @Test
  void shouldFailRequestingZeroDays() throws Exception {
    // given
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("days", "0").header("user-id", "100"));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(
        jsonPath("$.error").value("getSleepAverages.days: must be greater than or equal to 1"));
  }

  @Test
  void shouldFailRequesting91Days() throws Exception {
    // given
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("days", "91").header("user-id", "100"));

    // then
    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(
        jsonPath("$.error").value("getSleepAverages.days: must be less than or equal to 90"));
  }
}
