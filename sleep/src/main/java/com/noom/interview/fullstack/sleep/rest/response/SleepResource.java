package com.noom.interview.fullstack.sleep.rest.response;

import com.noom.interview.fullstack.sleep.rest.Mood;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SleepResource {

  @NotNull
  LocalTime sleepFrom;
  @NotNull
  LocalTime sleepTo;
  @NotNull
  LocalDate sleepDay;
  @NotNull
  LocalTime duration;
  @NotNull
  Mood mood;
}
