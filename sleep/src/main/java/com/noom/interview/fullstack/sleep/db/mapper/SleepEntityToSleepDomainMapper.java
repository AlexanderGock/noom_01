package com.noom.interview.fullstack.sleep.db.mapper;

import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.Sleep;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class SleepEntityToSleepDomainMapper {

  public Sleep mapToDomain(SleepEntity entity) {
    return Sleep.builder()
        .sleepFrom(entity.getSleepFrom().toLocalTime())
        .sleepTo(entity.getSleepTo().toLocalTime())
        .duration(mapDuration(entity.getDurationInSeconds()))
        .sleepDay(entity.getSleepDay().toLocalDate())
        .mood(Mood.valueOf(entity.getMood().name()))
        .build();
  }

  private Duration mapDuration(Integer durationInSeconds) {
    // records created before V1.2 have no duration persisted
    if (durationInSeconds == null) {
      return null;
    }
    return Duration.of(durationInSeconds, ChronoUnit.SECONDS);
  }
}
