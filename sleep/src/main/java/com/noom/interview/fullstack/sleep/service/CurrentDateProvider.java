package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class CurrentDateProvider implements ICurrentDateProvider {

  @Override
  public LocalDate getCurrentDate(User user) {
    return ZonedDateTime.now(ZoneId.of(user.getTimeZone())).toLocalDate();
  }
}
