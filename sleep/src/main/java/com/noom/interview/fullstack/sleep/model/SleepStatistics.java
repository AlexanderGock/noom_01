package com.noom.interview.fullstack.sleep.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SleepStatistics {

  LocalDate dayFrom;
  LocalDate dayTo;
  LocalTime averageTimeFrom;
  LocalTime averageTimeTo;
  Duration averageDuration;
  @Builder.Default
  Map<Mood, Integer> moodDistribution = new HashMap<>();
}
