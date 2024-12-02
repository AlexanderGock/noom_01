package com.noom.interview.fullstack.sleep.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.noom.interview.fullstack.sleep.db.entity.UserEntity;
import com.noom.interview.fullstack.sleep.db.mapper.UserEntityToUserDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.UserRepository;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepStatisticsDomainToRestMapper;
import com.noom.interview.fullstack.sleep.rest.response.SleepStatistics;
import com.noom.interview.fullstack.sleep.service.ISleepAveragesService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
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
class SleepAveragesControllerWebMvcTest {

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
  void shouldSucceedGettingSleepStatistics() throws Exception {
    // given
    SleepStatistics sleepStatistics = SleepStatistics.builder()
        .dayFrom(LocalDate.parse("2024-10-30"))
        .dayTo(LocalDate.parse("2024-11-29"))
        .averageDuration(LocalTime.parse("07:35:40"))
        .averageTimeFrom(LocalTime.parse("23:45:30"))
        .averageTimeTo(LocalTime.parse("07:03:15"))
        .moodDistribution(Map.of(Mood.BAD, 3, Mood.OK, 6, Mood.GOOD, 5))
        .build();
    given(sleepStatisticsDomainToRestMapper.mapToRest(any())).willReturn(sleepStatistics);
    given(userRepository.findById(anyLong())).willReturn(Optional.of(UserEntity.builder().build()));

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL).contentType(
        MediaType.APPLICATION_JSON_VALUE).header("user-id", "100"));

    // then
    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.error").doesNotExist());
    resultActions.andExpect(jsonPath("$.dayFrom").value("2024-10-30"));
    resultActions.andExpect(jsonPath("$.dayTo").value("2024-11-29"));
    resultActions.andExpect(jsonPath("$.averageDuration").value("07:35:40"));
    resultActions.andExpect(jsonPath("$.averageTimeFrom").value("23:45:30"));
    resultActions.andExpect(jsonPath("$.averageTimeTo").value("07:03:15"));
    resultActions.andExpect(jsonPath("$.moodDistribution.BAD").value("3"));
    resultActions.andExpect(jsonPath("$.moodDistribution.OK").value("6"));
    resultActions.andExpect(jsonPath("$.moodDistribution.GOOD").value("5"));
  }

  @Test
  void shouldFailWith401CodeDueToMissingHeader() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("days", "91"));

    // then
    resultActions.andExpect(status().isUnauthorized());
    verifyNoInteractions(userRepository);
  }

  @Test
  void shouldFailWith401CodeDueToMissingUser() throws Exception {
    // given
    given(userRepository.findById(anyLong())).willReturn(Optional.empty());

    // when
    ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("days", "91").header("user-id", "100"));

    // then
    resultActions.andExpect(status().isUnauthorized());
  }
}
