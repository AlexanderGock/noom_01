package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.User;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public interface IDurationService {

  Duration calculateDuration(User user, LocalDate date, LocalTime timeForm, LocalTime timeTo);
}
