package com.noom.interview.fullstack.sleep.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.rest.response.SleepResource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class SleepDomainToSleepResourceMapperUnitTest {

  private final SleepDomainToSleepResourceMapper sleepDomainToSleepResourceMapper = new SleepDomainToSleepResourceMapper();

  @Test
  void shouldMapToSleepResource() {
    // given
    Sleep sleep = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:30:00"))
        .sleepTo(LocalTime.parse("06:40:40"))
        .sleepDay(LocalDate.parse("2024-11-29"))
        .duration(Duration.of(33333L, ChronoUnit.SECONDS)) // 9h 15m 33s
        .mood(Mood.OK)
        .build();

    // when
    SleepResource sleepResource = sleepDomainToSleepResourceMapper.mapToSleepResource(sleep);

    // then
    assertThat(sleepResource).isNotNull();
    assertThat(sleepResource.getSleepFrom()).isEqualTo(LocalTime.parse("23:30:00"));
    assertThat(sleepResource.getSleepTo()).isEqualTo(LocalTime.parse("06:40:40"));
    assertThat(sleepResource.getSleepDay()).isEqualTo(LocalDate.parse("2024-11-29"));
    assertThat(sleepResource.getDuration()).isEqualTo(LocalTime.parse("09:15:33"));
    assertThat(sleepResource.getMood()).isEqualTo(com.noom.interview.fullstack.sleep.rest.Mood.OK);
  }
}
