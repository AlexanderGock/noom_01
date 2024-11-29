package com.noom.interview.fullstack.sleep.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.SleepStatistics;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder.In;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SleepStatisticsDomainToRestMapperUnitTest {

  private final SleepStatisticsDomainToRestMapper sleepStatisticsDomainToRestMapper = new SleepStatisticsDomainToRestMapper();

  @Test
  void shouldMapToRest() {
    // given
    Map<Mood, Integer> moodDistribution = Map.of(Mood.BAD, 3, Mood.OK, 4, Mood.GOOD, 5);
    SleepStatistics sleepStatistics = SleepStatistics.builder()
        .dayFrom(LocalDate.parse("2024-10-30"))
        .dayTo(LocalDate.parse("2024-11-29"))
        .averageTimeFrom(LocalTime.parse("22:34:56"))
        .averageTimeTo(LocalTime.parse("06:43:21"))
        .averageDuration(Duration.parse("PT8H8M25S"))
        .moodDistribution(moodDistribution)
        .build();

    // when
    com.noom.interview.fullstack.sleep.rest.response.SleepStatistics result =
        sleepStatisticsDomainToRestMapper.mapToRest(sleepStatistics);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getDayFrom()).isEqualTo(LocalDate.parse("2024-10-30"));
    assertThat(result.getDayTo()).isEqualTo(LocalDate.parse("2024-11-29"));
    assertThat(result.getAverageTimeFrom()).isEqualTo(LocalTime.parse("22:34:56"));
    assertThat(result.getAverageTimeTo()).isEqualTo(LocalTime.parse("06:43:21"));
    assertThat(result.getAverageDuration()).isEqualTo(LocalTime.parse("08:08:25"));
    assertThat(result.getMoodDistribution()).hasSize(3)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.BAD, 3)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.OK, 4)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.GOOD, 5);
  }

  @Test
  void shouldMapToRestIncompleteMoodDistribution() {
    // given
    Map<Mood, Integer> moodDistribution = Map.of(Mood.OK, 4);
    SleepStatistics sleepStatistics = SleepStatistics.builder()
        .moodDistribution(moodDistribution)
        .build();

    // when
    com.noom.interview.fullstack.sleep.rest.response.SleepStatistics result =
        sleepStatisticsDomainToRestMapper.mapToRest(sleepStatistics);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getMoodDistribution()).hasSize(3)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.BAD, 0)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.OK, 4)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.GOOD, 0);
  }

  @Test
  void shouldMapToRestEmptyResult() {
    // given
    SleepStatistics sleepStatistics = SleepStatistics.builder()
        .dayFrom(LocalDate.parse("2024-10-30"))
        .dayTo(LocalDate.parse("2024-11-29"))
        .build();

    // when
    com.noom.interview.fullstack.sleep.rest.response.SleepStatistics result =
        sleepStatisticsDomainToRestMapper.mapToRest(sleepStatistics);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getAverageTimeFrom()).isNull();
    assertThat(result.getAverageTimeTo()).isNull();
    assertThat(result.getAverageDuration()).isNull();
    assertThat(result.getMoodDistribution()).hasSize(3)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.BAD, 0)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.OK, 0)
        .containsEntry(com.noom.interview.fullstack.sleep.rest.Mood.GOOD, 0);
  }
}
