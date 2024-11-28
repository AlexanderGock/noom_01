package com.noom.interview.fullstack.sleep.db.entity;

import java.sql.Date;
import java.sql.Time;

public class SleepEntityBuilder extends SleepEntity.SleepEntityBuilder {

  public static SleepEntityBuilder aSleepEntity() {
    return new SleepEntityBuilder().withDefaultValues();
  }

  private SleepEntityBuilder withDefaultValues() {
    userId(1L);
    sleepFrom(Time.valueOf("22:00:00"));
    sleepTo(Time.valueOf("06:00:00"));
    sleepDay(Date.valueOf("2024-11-28"));
    mood(Mood.OK);
    return this;
  }
}
