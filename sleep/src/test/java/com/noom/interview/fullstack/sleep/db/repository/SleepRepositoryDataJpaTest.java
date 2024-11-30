package com.noom.interview.fullstack.sleep.db.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.noom.interview.fullstack.sleep.SleepApplication;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntityBuilder;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(SleepApplication.UNIT_TEST_PROFILE)
class SleepRepositoryDataJpaTest {

  @Autowired
  private SleepRepository sleepRepository;

  @Test
  void shouldFindRecordsForLast10Days() {
    // given
    SleepEntity entityOld = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-10"))
        .userId(1L)
        .build();
    sleepRepository.save(entityOld);
    SleepEntity entity10DaysAgo = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-18"))
        .userId(1L)
        .build();
    sleepRepository.save(entity10DaysAgo);
    SleepEntity entity5DaysAgo = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-23"))
        .userId(1L)
        .build();
    sleepRepository.save(entity5DaysAgo);
    SleepEntity entityToday = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-28"))
        .userId(1L)
        .build();
    sleepRepository.save(entityToday);
    SleepEntity entityOfOtherUser = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-24"))
        .userId(2L)
        .build();
    sleepRepository.save(entityOfOtherUser);

    // when
    Date startDate = Date.valueOf("2024-11-18");
    List<SleepEntity> sleepEntityList = sleepRepository.findAllByUserIdAndSleepDayGreaterThan(1L, startDate);

    // then
    assertThat(sleepEntityList).hasSize(2);
    sleepEntityList.sort(Comparator.comparing(SleepEntity::getSleepDay));
    assertThat(sleepEntityList.get(0).getSleepDay()).isEqualTo(Date.valueOf("2024-11-23"));
    assertThat(sleepEntityList.get(1).getSleepDay()).isEqualTo(Date.valueOf("2024-11-28"));
  }

  @Test
  void shouldFindRecordBySleepDate() {
    // given
    SleepEntity entityOld = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-17"))
        .userId(1L)
        .build();
    sleepRepository.save(entityOld);

    // when
    Optional<SleepEntity> sleepEntityOptional = sleepRepository.findOneByUserIdAndSleepDay(1L, Date.valueOf("2024-11-17"));

    // then
    assertThat(sleepEntityOptional).isPresent();
  }

  @Test
  void shouldNotFindRecordBySleepDate() {
    // given
    SleepEntity entityOld = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-16"))
        .userId(1L)
        .build();
    sleepRepository.save(entityOld);

    // when
    Optional<SleepEntity> sleepEntityOptional = sleepRepository.findOneByUserIdAndSleepDay(1L, Date.valueOf("2024-11-17"));

    // then
    assertThat(sleepEntityOptional).isEmpty();
  }

  @Test
  void shouldExistRecordBySleepDate() {
    // given
    SleepEntity entityOld = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-15"))
        .userId(1L)
        .build();
    sleepRepository.save(entityOld);

    // when
    boolean exists = sleepRepository.existsByUserIdAndSleepDay(1L, Date.valueOf("2024-11-15"));

    // then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldNotExistRecordBySleepDate() {
    // given
    SleepEntity entityOld = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-13"))
        .userId(1L)
        .build();
    sleepRepository.save(entityOld);

    // when
    boolean exists = sleepRepository.existsByUserIdAndSleepDay(1L, Date.valueOf("2024-11-14"));

    // then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldFailToCreateRecordForSameDaySameUser() {
    // given
    SleepEntity existingEntity = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-13"))
        .userId(1L)
        .build();
    sleepRepository.save(existingEntity);

    // when // then
    SleepEntity newEntity = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-13"))
        .userId(1L)
        .build();
    assertThatExceptionOfType(Exception.class).isThrownBy(() -> sleepRepository.save(newEntity));
  }
}
