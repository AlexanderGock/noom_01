package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.db.mapper.SleepDomainToSleepEntityMapper;
import com.noom.interview.fullstack.sleep.db.mapper.SleepEntityToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.SleepRepository;
import com.noom.interview.fullstack.sleep.exception.RecordAlreadyExistsException;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SleepManagementService implements ISleepManagementService {

  private final SleepDomainToSleepEntityMapper sleepDomainToSleepEntityMapper;
  private final SleepEntityToSleepDomainMapper sleepEntityToSleepDomainMapper;
  private final SleepRepository sleepRepository;

  @Override
  @Transactional
  public Sleep createSleep(Sleep sleep, User user) {
    LocalDate today = LocalDate.now();

    if (sleepRepository.existsByUserIdAndSleepDay(user.getId(), Date.valueOf(today))) {
      throw new RecordAlreadyExistsException("Sleep record for today already exists");
    }

    SleepEntity entity = sleepDomainToSleepEntityMapper.mapToSleepEntity(sleep, user, today);
    log.info("Persist new sleep record: {}", entity);
    SleepEntity persistedEntity = sleepRepository.save(entity);
    return sleepEntityToSleepDomainMapper.mapToDomain(persistedEntity);
  }
}
