package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.SleepStatistics;
import com.noom.interview.fullstack.sleep.model.User;

public interface ISleepAveragesService {

  SleepStatistics getAverageValues(User user, int days);
}
