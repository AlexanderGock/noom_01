package com.noom.interview.fullstack.sleep.rest.mapper;

import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import org.springframework.stereotype.Component;

@Component
public class SleepDtoToSleepDomainMapper {

  public Sleep mapToDomain(SleepDto sleepDto) {
    return Sleep.builder()
        .sleepFrom(sleepDto.getSleepFrom())
        .sleepTo(sleepDto.getSleepTo())
        .mood(Mood.valueOf(sleepDto.getMood().name()))
        .build();
  }
}
