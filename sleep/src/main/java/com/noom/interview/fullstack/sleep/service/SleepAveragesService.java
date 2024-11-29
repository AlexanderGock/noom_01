package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.db.mapper.SleepEntityToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.SleepRepository;
import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.SleepStatistics;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SleepAveragesService implements ISleepAveragesService {

  private final SleepRepository sleepRepository;
  private final SleepEntityToSleepDomainMapper sleepEntityToSleepDomainMapper;
  private final AverageTimeHelper averageTimeHelper;

  @Override
  public SleepStatistics getAverageValues(User user, int days) {
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusDays(days);
    List<SleepEntity> entities = sleepRepository.findAllByUserIdAndSleepDayGreaterThan(user.getId(),
        Date.valueOf(startDate));

    List<Sleep> sleeps = entities.stream().map(sleepEntityToSleepDomainMapper::mapToDomain).collect(
        Collectors.toList());

    return SleepStatistics.builder()
        .dayFrom(startDate)
        .dayTo(today)
        .averageTimeFrom(averageTimeHelper.getAverageTimeFrom(sleeps))
        .averageTimeTo(averageTimeHelper.getAverageTimeTo(sleeps))
        .averageDuration(
            averageTimeHelper.getAverageDuration(sleeps.stream().map(Sleep::getDuration).collect(
                Collectors.toList())))
        .moodDistribution(buildMoodDistribution(sleeps))
        .build();
  }

  private Map<Mood, Integer> buildMoodDistribution(List<Sleep> sleeps) {
    return sleeps.stream()
        .collect(Collectors.groupingBy(Sleep::getMood, Collectors.summingInt(m -> 1)));
  }
}
