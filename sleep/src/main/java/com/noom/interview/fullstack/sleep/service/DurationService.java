package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.User;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class DurationService implements IDurationService {

  @Override
  public Duration calculateDuration(User user, LocalDate date, LocalTime timeForm, LocalTime timeTo) {
    LocalDate dateFrom = (timeForm.isBefore(timeTo)) ? date : date.minusDays(1);
    ZoneId zoneId = ZoneId.of(user.getTimeZone());
    ZonedDateTime start = ZonedDateTime.of(dateFrom, timeForm, zoneId);
    ZonedDateTime end = ZonedDateTime.of(date, timeTo, zoneId);
    return Duration.between(start, end);
  }
}
