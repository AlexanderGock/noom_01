package com.noom.interview.fullstack.sleep.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.model.Sleep;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class AverageTimeHelperUnitTest {

  private final AverageTimeHelper averageTimeHelper = new AverageTimeHelper();

  @Test
  void shouldCalculateAverageTimeFromWhenAllValuesBeforeMidnight() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:30"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep2 = Sleep.builder()
        .sleepFrom(LocalTime.parse("22:10"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep3 = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:05"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();

    // when
    LocalTime averageTimeForm = averageTimeHelper.getAverageTimeFrom(List.of(sleep1, sleep2, sleep3));

    // then
    assertThat(averageTimeForm).isNotNull().isEqualTo(LocalTime.parse("22:55"));
  }

  @Test
  void shouldCalculateAverageTimeFromWhenAllValuesAfterMidnight() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:10"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep2 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:00"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep3 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:35"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();

    // when
    LocalTime averageTimeForm = averageTimeHelper.getAverageTimeFrom(List.of(sleep1, sleep2, sleep3));

    // then
    assertThat(averageTimeForm).isNotNull().isEqualTo(LocalTime.parse("00:15"));
  }

  @Test
  void shouldNotFailWhenCalculatingAverageTimeFromWithEmptyData() {
    // given

    // when
    LocalTime averageTimeForm = averageTimeHelper.getAverageTimeFrom(Collections.emptyList());

    // then
    assertThat(averageTimeForm).isNull();
  }

  @Test
  void shouldCalculateAverageTimeFromWhenMixedValuesCase1() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:10"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep2 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:50"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep3 = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:30"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();

    // when
    LocalTime averageTimeForm = averageTimeHelper.getAverageTimeFrom(List.of(sleep1, sleep2, sleep3));

    // then
    assertThat(averageTimeForm).isNotNull().isEqualTo(LocalTime.parse("00:10"));
  }

  @Test
  void shouldCalculateAverageTimeFromWhenMixedValuesCase2() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:10"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep2 = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:50"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep3 = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:30"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();

    // when
    LocalTime averageTimeForm = averageTimeHelper.getAverageTimeFrom(List.of(sleep1, sleep2, sleep3));

    // then
    assertThat(averageTimeForm).isNotNull().isEqualTo(LocalTime.parse("23:50"));
  }

  @Test
  void shouldCalculateAverageTimeFromWhenSingleMidnightValuePassed() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepFrom(LocalTime.parse("00:00"))
        .sleepTo(LocalTime.parse("07:00"))
        .build();

    // when
    LocalTime averageTimeForm = averageTimeHelper.getAverageTimeFrom(List.of(sleep1));

    // then
    assertThat(averageTimeForm).isNotNull().isEqualTo(LocalTime.parse("00:00"));
  }

  @Test
  void shouldCalculateAverageTimeTo() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepTo(LocalTime.parse("07:00"))
        .build();
    Sleep sleep2 = Sleep.builder()
        .sleepTo(LocalTime.parse("08:00"))
        .build();
    Sleep sleep3 = Sleep.builder()
        .sleepTo(LocalTime.parse("06:50"))
        .build();

    // when
    LocalTime averageTimeTo = averageTimeHelper.getAverageTimeTo(List.of(sleep1, sleep2, sleep3));

    // then
    assertThat(averageTimeTo).isNotNull().isEqualTo(LocalTime.parse("07:16:40"));
  }

  @Test
  void shouldCalculateAverageTimeToForSingleValue() {
    // given
    Sleep sleep1 = Sleep.builder()
        .sleepTo(LocalTime.parse("06:30"))
        .build();

    // when
    LocalTime averageTimeTo = averageTimeHelper.getAverageTimeTo(List.of(sleep1));

    // then
    assertThat(averageTimeTo).isNotNull().isEqualTo(LocalTime.parse("06:30:00"));
  }

  @Test
  void shouldNotFailWhenCalculatingAverageTimeToWithEmptyData() {
    // given

    // when
    LocalTime averageTimeTo = averageTimeHelper.getAverageTimeTo(Collections.emptyList());

    // then
    assertThat(averageTimeTo).isNull();
  }

  @Test
  void shouldGetAverageDuration() {
    // given
    Duration duration1 = Duration.parse("PT7H30M");
    Duration duration2 = Duration.parse("PT8H05M");
    Duration duration3 = Duration.parse("PT7H20M");

    // when
    Duration result = averageTimeHelper.getAverageDuration(List.of(duration1, duration2, duration3));

    // then
    assertThat(result).isNotNull().isEqualTo(Duration.parse("PT7H38M20S"));
  }

  @Test
  void shouldGetAverageDurationForSingleValue() {
    // given
    Duration duration1 = Duration.parse("PT7H30M");

    // when
    Duration result = averageTimeHelper.getAverageDuration(List.of(duration1));

    // then
    assertThat(result).isNotNull().isEqualTo(Duration.parse("PT7H30M"));
  }

  @Test
  void shouldNotFailWhenGettingAverageDurationWithEmptyData() {
    // given

    // when
    Duration result = averageTimeHelper.getAverageDuration(Collections.emptyList());

    // then
    assertThat(result).isNull();
  }
}
