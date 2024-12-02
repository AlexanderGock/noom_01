package com.noom.interview.fullstack.sleep.db.mapper;

import com.noom.interview.fullstack.sleep.db.entity.UserEntity;
import com.noom.interview.fullstack.sleep.model.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserDomainMapper {

  private static final String DEFAULT_TIME_ZONE = "UTC";

  public User mapToDomain(UserEntity entity) {
    return User.builder()
        .id(entity.getId())
        .timeZone(ObjectUtils.defaultIfNull(entity.getTimeZone(), DEFAULT_TIME_ZONE))
        .build();
  }
}
