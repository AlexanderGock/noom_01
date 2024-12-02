package com.noom.interview.fullstack.sleep.db.mapper;

import com.noom.interview.fullstack.sleep.db.entity.Mood;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class SleepDomainToSleepEntityMapper {

  public SleepEntity mapToSleepEntity(Sleep sleep, User user, LocalDate sleepDay, Duration duration) {
    return SleepEntity.builder()
        .userId(user.getId())
        .sleepFrom(Time.valueOf(sleep.getSleepFrom()))
        .sleepTo(Time.valueOf(sleep.getSleepTo()))
        .sleepDay(Date.valueOf(sleepDay))
        .mood(Mood.valueOf(sleep.getMood().name()))
        .durationInSeconds((int)duration.toSeconds())
        .build();
  }
}
