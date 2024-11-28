package com.noom.interview.fullstack.sleep.db.repository;

import static org.assertj.core.api.Assertions.assertThat;
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
  void shouldFindSingleLatestRecord() {
    // given
    SleepEntity entity1 = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-10"))
        .userId(1L)
        .build();
    sleepRepository.save(entity1);
    SleepEntity entity2 = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-09"))
        .userId(1L)
        .build();
    sleepRepository.save(entity2);
    SleepEntity entity3 = SleepEntityBuilder.aSleepEntity()
        .sleepDay(Date.valueOf("2024-11-11"))
        .userId(2L)
        .build();
    sleepRepository.save(entity3);

    // when
    Optional<SleepEntity> sleepEntityOptional = sleepRepository.findFirstByUserIdOrderBySleepDayDesc(1L);

    // then
    assertThat(sleepEntityOptional).isPresent();
    assertThat(sleepEntityOptional.get().getSleepDay()).isEqualTo(Date.valueOf("2024-11-10"));
  }

  @Test
  void shouldNotFindLatestRecord() {
    // given

    // when
    Optional<SleepEntity> sleepEntityOptional = sleepRepository.findFirstByUserIdOrderBySleepDayDesc(1L);

    // then
    assertThat(sleepEntityOptional).isEmpty();
  }

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
}
