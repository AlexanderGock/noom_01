package com.noom.interview.fullstack.sleep.rest.mapper;

import com.noom.interview.fullstack.sleep.rest.Mood;
import com.noom.interview.fullstack.sleep.rest.response.SleepStatistics;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class SleepStatisticsDomainToRestMapper {

  public SleepStatistics mapToRest(
      com.noom.interview.fullstack.sleep.model.SleepStatistics sleepStatistics) {
    return SleepStatistics.builder()
        .dayFrom(sleepStatistics.getDayFrom())
        .dayTo(sleepStatistics.getDayTo())
        .averageTimeFrom(sleepStatistics.getAverageTimeFrom())
        .averageTimeTo(sleepStatistics.getAverageTimeTo())
        .averageDuration(mapDuration(sleepStatistics.getAverageDuration()))
        .moodDistribution(mapMoodDistribution(sleepStatistics.getMoodDistribution()))
        .build();
  }

  private LocalTime mapDuration(Duration duration) {
    if (duration == null) {
      return null;
    }
    return LocalTime.of(duration.toHoursPart(),
        duration.toMinutesPart(),
        duration.toSecondsPart());
  }

  private Map<Mood, Integer> mapMoodDistribution(
      Map<com.noom.interview.fullstack.sleep.model.Mood, Integer> moodDistribution) {
    Map<Mood, Integer> result = new HashMap<>();
    for (Mood mood : Mood.values()) {
      Integer number = moodDistribution.get(
          com.noom.interview.fullstack.sleep.model.Mood.valueOf(mood.name()));
      result.put(mood, ObjectUtils.defaultIfNull(number, 0));
    }
    return result;
  }
}
