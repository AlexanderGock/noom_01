package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.db.mapper.SleepDomainToSleepEntityMapper;
import com.noom.interview.fullstack.sleep.db.mapper.SleepEntityToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.SleepRepository;
import com.noom.interview.fullstack.sleep.exception.RecordAlreadyExistsException;
import com.noom.interview.fullstack.sleep.exception.SleepNotFoundException;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SleepManagementService implements ISleepManagementService {

  private final static String SLEEP_NOT_FOUND_ERROR_PATTERN = "No sleep record found for user %s at %s";

  private final SleepDomainToSleepEntityMapper sleepDomainToSleepEntityMapper;
  private final SleepEntityToSleepDomainMapper sleepEntityToSleepDomainMapper;
  private final SleepRepository sleepRepository;
  private final ICurrentDateProvider currentDateProvider;
  private final IDurationService durationService;

  @Override
  @Transactional
  public Sleep createSleep(Sleep sleep, User user) {
    LocalDate today = currentDateProvider.getCurrentDate(user);

    if (sleepRepository.existsByUserIdAndSleepDay(user.getId(), Date.valueOf(today))) {
      throw new RecordAlreadyExistsException("Sleep record for today already exists");
    }

    Duration duration = durationService.calculateDuration(user, today, sleep.getSleepFrom(), sleep.getSleepTo());
    SleepEntity entity = sleepDomainToSleepEntityMapper.mapToSleepEntity(sleep, user, today, duration);
    log.info("Persist new sleep record: {}", entity);
    SleepEntity persistedEntity = sleepRepository.save(entity);
    return sleepEntityToSleepDomainMapper.mapToDomain(persistedEntity);
  }

  @Override
  public Sleep getLastNightSleep(User user) {
    LocalDate today = currentDateProvider.getCurrentDate(user);

    Optional<SleepEntity> entityOptional = sleepRepository.findOneByUserIdAndSleepDay(user.getId(), Date.valueOf(today));

    Sleep sleep = entityOptional
        .map(sleepEntityToSleepDomainMapper::mapToDomain)
        .orElseThrow(() -> new SleepNotFoundException(String.format(SLEEP_NOT_FOUND_ERROR_PATTERN, user.getId(), today)));

    // records created before V1.2 have no duration persisted
    if (sleep.getDuration() != null) {
      return sleep;
    }
    Duration duration = durationService.calculateDuration(user, sleep.getSleepDay(), sleep.getSleepFrom(), sleep.getSleepTo());
    return sleep.toBuilder().duration(duration).build();
  }
}
