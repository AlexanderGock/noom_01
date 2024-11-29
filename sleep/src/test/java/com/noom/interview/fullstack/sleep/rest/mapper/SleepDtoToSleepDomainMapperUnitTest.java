package com.noom.interview.fullstack.sleep.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.rest.Mood;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class SleepDtoToSleepDomainMapperUnitTest {

  private final SleepDtoToSleepDomainMapper sleepDtoToSleepDomainMapper = new SleepDtoToSleepDomainMapper();

  @Test
  void shouldMapToDomain() {
    // given
    SleepDto sleepDto = SleepDto.builder()
        .sleepFrom(LocalTime.parse("23:30:00"))
        .sleepTo(LocalTime.parse("06:40:40"))
        .mood(Mood.BAD)
        .build();

    // when
    Sleep sleep = sleepDtoToSleepDomainMapper.mapToDomain(sleepDto);

    // then
    assertThat(sleep).isNotNull();
    assertThat(sleep.getSleepFrom()).isEqualTo(LocalTime.parse("23:30:00"));
    assertThat(sleep.getSleepTo()).isEqualTo(LocalTime.parse("06:40:40"));
    assertThat(sleep.getMood()).isEqualTo(com.noom.interview.fullstack.sleep.model.Mood.BAD);
  }
}
