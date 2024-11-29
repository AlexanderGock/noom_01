package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.Sleep;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class AverageTimeHelper {

  private final Duration DAY = Duration.ofDays(1);
  private final LocalTime MIDNIGHT = LocalTime.of(0, 0, 0);

  public LocalTime getAverageTimeTo(List<Sleep> sleepList) {
    if (CollectionUtils.isEmpty(sleepList)) {
      return null;
    }

    Duration duration = Duration.ZERO;
    for (Sleep sleep: sleepList) {
      duration = duration.plus(timeToDuration(sleep.getSleepTo()));
    }
    duration = duration.dividedBy(sleepList.size());

    return LocalTime.of(duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
  }

  public LocalTime getAverageTimeFrom(List<Sleep> sleepList) {
    if (CollectionUtils.isEmpty(sleepList)) {
      return null;
    }

    Duration duration = Duration.ZERO;
    for (Sleep sleep: sleepList) {
      if (sleep.getSleepFrom().isBefore(sleep.getSleepTo())) {
        // fall asleep after midnight
        duration = duration.plus(timeToDuration(sleep.getSleepFrom()));
      } else {
        // fall asleep before midnight
        duration = duration.plus(timeToDuration(sleep.getSleepFrom())).minus(DAY);
      }
    }
    duration = duration.dividedBy(sleepList.size());

    if (duration.isNegative()) {
      // average before midnight
      Duration timeAsDuration = DAY.plus(duration);
      return LocalTime.of(timeAsDuration.toHoursPart(), timeAsDuration.toMinutesPart(), timeAsDuration.toSecondsPart());
    } else {
      // average after midnight
      return LocalTime.of(duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }
  }

  public Duration getAverageDuration(List<Duration> durationList) {
    if (CollectionUtils.isEmpty(durationList)) {
      return null;
    }

    Duration totalDuration = durationList.stream().reduce(Duration.ZERO, Duration::plus);
    return totalDuration.dividedBy(durationList.size());
  }

  private Duration timeToDuration(LocalTime time) {
    return Duration.between(MIDNIGHT, time);
  }
}
