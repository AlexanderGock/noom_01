package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;

public interface ISleepManagementService {

  Sleep createSleep(Sleep sleep, User user);

  Sleep getLastNightSleep(User user);
}
