package com.noom.interview.fullstack.sleep.rest.response;

import com.noom.interview.fullstack.sleep.rest.Mood;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SleepStatistics {

  @NotNull
  LocalDate dayFrom;
  @NotNull
  LocalDate dayTo;
  LocalTime averageTimeFrom;
  LocalTime averageTimeTo;
  LocalTime averageDuration;
  Map<Mood, Integer> moodDistribution;
}
