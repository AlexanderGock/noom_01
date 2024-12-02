package com.noom.interview.fullstack.sleep.db.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import com.noom.interview.fullstack.sleep.db.entity.UserEntity;
import com.noom.interview.fullstack.sleep.model.User;
import org.junit.jupiter.api.Test;

class UserEntityToUserDomainMapperUnitTest {

  private final UserEntityToUserDomainMapper userEntityToUserDomainMapper = new UserEntityToUserDomainMapper();

  @Test
  void shouldMapToDomain() {
    // given
    UserEntity entity = UserEntity.builder()
        .id(111L)
        .timeZone("Europe/Warsaw")
        .build();

    // when
    User user = userEntityToUserDomainMapper.mapToDomain(entity);

    // then
    assertThat(user).isNotNull();
    assertThat(user.getId()).isEqualTo(111L);
    assertThat(user.getTimeZone()).isEqualTo("Europe/Warsaw");
  }

  @Test
  void shouldMapToDomainWithDefaultTimeZone() {
    // given
    UserEntity entity = UserEntity.builder()
        .id(222L)
        .build();

    // when
    User user = userEntityToUserDomainMapper.mapToDomain(entity);

    // then
    assertThat(user).isNotNull();
    assertThat(user.getId()).isEqualTo(222L);
    assertThat(user.getTimeZone()).isEqualTo("UTC");
  }
}
