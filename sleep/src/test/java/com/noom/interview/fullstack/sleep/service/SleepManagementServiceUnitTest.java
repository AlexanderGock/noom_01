package com.noom.interview.fullstack.sleep.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import com.noom.interview.fullstack.sleep.db.mapper.SleepDomainToSleepEntityMapper;
import com.noom.interview.fullstack.sleep.db.mapper.SleepEntityToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.SleepRepository;
import com.noom.interview.fullstack.sleep.exception.RecordAlreadyExistsException;
import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import java.sql.Date;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SleepManagementServiceUnitTest {

  @Mock
  private SleepDomainToSleepEntityMapper sleepDomainToSleepEntityMapper;

  @Mock
  private SleepEntityToSleepDomainMapper sleepEntityToSleepDomainMapper;

  @Mock
  private SleepRepository sleepRepository;

  @InjectMocks
  private SleepManagementService sleepManagementService;

  @Test
  void shouldCreateSleep() {
    // given
    Sleep sleep = Sleep.builder().build();
    User user = User.builder().id(111L).build();
    SleepEntity entity = SleepEntity.builder().build();

    given(sleepRepository.existsByUserIdAndSleepDay(anyLong(), any(Date.class))).willReturn(false);
    given(sleepDomainToSleepEntityMapper.mapToSleepEntity(any(Sleep.class), any(User.class), any(
            LocalDate.class))).willReturn(entity);
    given(sleepRepository.save(any(SleepEntity.class))).willReturn(entity);
    given(sleepEntityToSleepDomainMapper.mapToDomain(any(SleepEntity.class))).willReturn(sleep);

    // when
    Sleep result = sleepManagementService.createSleep(sleep, user);

    // then
    assertThat(result).isNotNull();

    verify(sleepRepository).existsByUserIdAndSleepDay(eq(111L), any(Date.class));
    verify(sleepDomainToSleepEntityMapper).mapToSleepEntity(eq(sleep), eq(user), any(
        LocalDate.class));
    verify(sleepRepository).save(entity);
    verify(sleepEntityToSleepDomainMapper).mapToDomain(entity);
  }

  @Test
  void shouldFailCreatingSleepDueToDuplicate() {
    // given
    Sleep sleep = Sleep.builder().build();
    User user = User.builder().id(111L).build();

    given(sleepRepository.existsByUserIdAndSleepDay(anyLong(), any(Date.class))).willReturn(true);

    // when
    assertThatExceptionOfType(RecordAlreadyExistsException.class).isThrownBy(
        () -> sleepManagementService.createSleep(sleep, user));

    // then
    verify(sleepRepository).existsByUserIdAndSleepDay(eq(111L), any(Date.class));
    verify(sleepRepository, never()).save(any(SleepEntity.class));
  }
}
