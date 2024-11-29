package com.noom.interview.fullstack.sleep.db.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.db.entity.Mood;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.model.Sleep;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class SleepEntityToSleepDomainMapperUnitTest {

  private final SleepEntityToSleepDomainMapper sleepEntityToSleepDomainMapper = new SleepEntityToSleepDomainMapper();

  @Test
  void shouldMapToDomainWhenGoToBadAfterMidnight() {
    // given
    SleepEntity entity = SleepEntity.builder()
        .sleepFrom(Time.valueOf("00:15:30"))
        .sleepTo(Time.valueOf("08:00:00"))
        .sleepDay(Date.valueOf("2014-11-29"))
        .mood(Mood.OK)
        .build();

    // when
    Sleep sleep = sleepEntityToSleepDomainMapper.mapToDomain(entity);

    // then
    assertThat(sleep).isNotNull();
    assertThat(sleep.getSleepFrom()).isEqualTo(LocalTime.parse("00:15:30"));
    assertThat(sleep.getSleepTo()).isEqualTo(LocalTime.parse("08:00:00"));
    assertThat(sleep.getSleepDay()).isEqualTo(LocalDate.parse("2014-11-29"));
    assertThat(sleep.getMood()).isEqualTo(com.noom.interview.fullstack.sleep.model.Mood.OK);
    assertThat(sleep.getDuration()).isEqualTo(Duration.parse("PT7H44M30S")); //"07:44:30"
  }

  @Test
  void shouldMapToDomainWhenGoToBadBeforeMidnight() {
    // given
    SleepEntity entity = SleepEntity.builder()
        .sleepFrom(Time.valueOf("23:15:30"))
        .sleepTo(Time.valueOf("08:00:00"))
        .sleepDay(Date.valueOf("2014-11-29"))
        .mood(Mood.OK)
        .build();

    // when
    Sleep sleep = sleepEntityToSleepDomainMapper.mapToDomain(entity);

    // then
    assertThat(sleep).isNotNull();
    assertThat(sleep.getDuration()).isEqualTo(Duration.parse("PT8H44M30S")); //"08:44:30"
  }

  @Test
  void shouldMapToDomainWhenGoToBadAtMidnight() {
    // given
    SleepEntity entity = SleepEntity.builder()
        .sleepFrom(Time.valueOf("00:00:00"))
        .sleepTo(Time.valueOf("08:00:00"))
        .sleepDay(Date.valueOf("2014-11-29"))
        .mood(Mood.OK)
        .build();

    // when
    Sleep sleep = sleepEntityToSleepDomainMapper.mapToDomain(entity);

    // then
    assertThat(sleep).isNotNull();
    assertThat(sleep.getDuration()).isEqualTo(Duration.parse("PT8H")); //"08:00:00"
  }
}
