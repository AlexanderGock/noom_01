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
        .duration(calculateDuration(entity))
        .sleepDay(entity.getSleepDay().toLocalDate())
        .mood(Mood.valueOf(entity.getMood().name()))
        .build();
  }

  private Duration calculateDuration(SleepEntity entity) {
    if (entity.getSleepFrom().before(entity.getSleepTo())) {
      return Duration.between(entity.getSleepFrom().toLocalTime(), entity.getSleepTo().toLocalTime());
    } else {
      return Duration.of(24L, ChronoUnit.HOURS).minus(Duration.between(entity.getSleepTo().toLocalTime(), entity.getSleepFrom().toLocalTime()));
    }
  }
}
