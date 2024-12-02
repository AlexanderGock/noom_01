package com.noom.interview.fullstack.sleep.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.model.User;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class DurationServiceUnitTest {

  private final DurationService durationService = new DurationService();

  @Test
  void shouldCalculateDurationWhenFallAsleepBeforeMidnight() {
    // given
    User user = User.builder()
        .timeZone("Europe/Warsaw")
        .build();

    // when
    Duration duration = durationService.calculateDuration(user, LocalDate.parse("2024-12-02"),
        LocalTime.parse("23:10:00"), LocalTime.parse("06:00:00"));

    // then
    assertThat(duration).isEqualTo(Duration.parse("PT6H50M"));
  }

  @Test
  void shouldCalculateDurationWhenFallAsleepAfterMidnight() {
    // given
    User user = User.builder()
        .timeZone("Europe/Warsaw")
        .build();

    // when
    Duration duration = durationService.calculateDuration(user, LocalDate.parse("2024-12-02"),
        LocalTime.parse("00:30:00"), LocalTime.parse("08:00:00"));

    // then
    assertThat(duration).isEqualTo(Duration.parse("PT7H30M"));
  }

  @Test
  void shouldCalculateDurationWhenAutumnDayLightSavingOccurs() {
    // given
    User user = User.builder()
        .timeZone("Europe/Warsaw")
        .build();

    // when
    Duration duration = durationService.calculateDuration(user, LocalDate.parse("2024-10-27"),
        LocalTime.parse("23:00:00"), LocalTime.parse("06:00:00"));

    // then
    assertThat(duration).isEqualTo(Duration.parse("PT8H"));
  }

  @Test
  void shouldCalculateDurationWhenSpringDayLightSavingOccurs() {
    // given
    User user = User.builder()
        .timeZone("Europe/Warsaw")
        .build();

    // when
    Duration duration = durationService.calculateDuration(user, LocalDate.parse("2024-03-31"),
        LocalTime.parse("23:00:00"), LocalTime.parse("06:00:00"));

    // then
    assertThat(duration).isEqualTo(Duration.parse("PT6H"));
  }
}
