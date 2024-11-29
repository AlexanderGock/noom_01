package com.noom.interview.fullstack.sleep.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Sleep {

  LocalTime sleepFrom;

  LocalTime sleepTo;

  Duration duration;

  LocalDate sleepDay;

  Mood mood;
}
