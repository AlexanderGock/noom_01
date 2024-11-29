package com.noom.interview.fullstack.sleep.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.db.mapper.SleepEntityToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.SleepRepository;
import com.noom.interview.fullstack.sleep.model.Mood;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.SleepStatistics;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SleepAveragesServiceUnitTest {

  @Mock
  private SleepRepository sleepRepository;

  @Mock
  private SleepEntityToSleepDomainMapper sleepEntityToSleepDomainMapper;

  @Mock
  private AverageTimeHelper averageTimeHelper;

  @InjectMocks
  private SleepAveragesService sleepAveragesService;

  @Test
  void shouldGetAverageValues() {
    // given
    User user = User.builder().id(1L).build();
    SleepEntity entity = SleepEntity.builder().build();
    Sleep sleep1 = Sleep.builder().mood(Mood.OK).build();
    Sleep sleep2 = Sleep.builder().mood(Mood.GOOD).build();
    Sleep sleep3 = Sleep.builder().mood(Mood.OK).build();

    given(sleepRepository.findAllByUserIdAndSleepDayGreaterThan(anyLong(),
        any(Date.class))).willReturn(
        List.of(entity, entity, entity));
    given(sleepEntityToSleepDomainMapper.mapToDomain(any(SleepEntity.class))).willReturn(sleep1,
        sleep2, sleep3);

    // when
    SleepStatistics sleepStatistics = sleepAveragesService.getAverageValues(user, 30);

    // then
    assertThat(sleepStatistics).isNotNull();
    assertThat(sleepStatistics.getDayTo().minusDays(30)).isEqualTo(sleepStatistics.getDayFrom());
    assertThat(sleepStatistics.getMoodDistribution()).hasSize(2)
        .containsEntry(Mood.OK, 2)
        .containsEntry(Mood.GOOD, 1);

    verify(sleepRepository).findAllByUserIdAndSleepDayGreaterThan(eq(1L), any(Date.class));
    verify(sleepEntityToSleepDomainMapper, times(3)).mapToDomain(any(SleepEntity.class));
    verify(averageTimeHelper).getAverageDuration(anyList());
    verify(averageTimeHelper).getAverageTimeFrom(anyList());
    verify(averageTimeHelper).getAverageTimeTo(anyList());
  }

  @Test
  void shouldHandleEmptyEntitiesList() {
    // given
    User user = User.builder().id(1L).build();

    given(sleepRepository.findAllByUserIdAndSleepDayGreaterThan(anyLong(),
        any(Date.class))).willReturn(
        Collections.emptyList());

    // when
    SleepStatistics sleepStatistics = sleepAveragesService.getAverageValues(user, 30);

    // then
    assertThat(sleepStatistics).isNotNull();
    assertThat(sleepStatistics.getMoodDistribution()).isEmpty();

    verify(sleepRepository).findAllByUserIdAndSleepDayGreaterThan(eq(1L), any(Date.class));
    verifyNoInteractions(sleepEntityToSleepDomainMapper);
    verify(averageTimeHelper).getAverageDuration(anyList());
    verify(averageTimeHelper).getAverageTimeFrom(anyList());
    verify(averageTimeHelper).getAverageTimeTo(anyList());
  }
}
