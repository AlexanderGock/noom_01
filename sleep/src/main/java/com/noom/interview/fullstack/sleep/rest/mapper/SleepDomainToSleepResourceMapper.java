package com.noom.interview.fullstack.sleep.rest.mapper;

import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.rest.Mood;
import com.noom.interview.fullstack.sleep.rest.response.SleepResource;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class SleepDomainToSleepResourceMapper {

  public SleepResource mapToSleepResource(Sleep sleep) {
    return SleepResource.builder()
        .sleepFrom(sleep.getSleepFrom())
        .sleepTo(sleep.getSleepTo())
        .sleepDay(sleep.getSleepDay())
        .duration(LocalTime.of(sleep.getDuration().toHoursPart(), sleep.getDuration().toMinutesPart(), sleep.getDuration().toSecondsPart()))
        .mood(Mood.valueOf(sleep.getMood().name()))
        .build();
  }
}
