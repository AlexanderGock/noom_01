package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.User;
import java.time.LocalDate;

public interface ICurrentDateProvider {

  LocalDate getCurrentDate(User user);
}
