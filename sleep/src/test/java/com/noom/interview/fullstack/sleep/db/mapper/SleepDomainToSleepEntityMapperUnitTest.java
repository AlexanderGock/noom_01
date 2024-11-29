package com.noom.interview.fullstack.sleep.db.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class SleepDomainToSleepEntityMapperUnitTest {

  private final SleepDomainToSleepEntityMapper sleepDomainToSleepEntityMapper = new SleepDomainToSleepEntityMapper();

  @Test
  void shouldMapToSleepEntity() {
    // given
    Sleep sleep = Sleep.builder()
        .sleepFrom(LocalTime.parse("23:30:00"))
        .sleepTo(LocalTime.parse("06:40:40"))
        .sleepDay(LocalDate.parse("2024-11-29"))
        .duration(Duration.of(33333L, ChronoUnit.SECONDS)) // 9h 15m 33s
        .mood(Mood.OK)
        .build();
    User user = User.builder().id(123L).build();
    LocalDate today = LocalDate.parse("2024-11-29");

    // when
    SleepEntity sleepEntity = sleepDomainToSleepEntityMapper.mapToSleepEntity(sleep, user, today);

    // then
    assertThat(sleepEntity).isNotNull();
    assertThat(sleepEntity.getSleepFrom()).isEqualTo(Time.valueOf("23:30:00"));
    assertThat(sleepEntity.getSleepTo()).isEqualTo(Time.valueOf("06:40:40"));
    assertThat(sleepEntity.getSleepDay()).isEqualTo(Date.valueOf("2024-11-29"));
    assertThat(sleepEntity.getMood()).isEqualTo(com.noom.interview.fullstack.sleep.db.entity.Mood.OK);
    assertThat(sleepEntity.getUserId()).isEqualTo(123L);
  }
}
