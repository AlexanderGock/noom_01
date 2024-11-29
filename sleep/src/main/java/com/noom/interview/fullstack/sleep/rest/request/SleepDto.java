package com.noom.interview.fullstack.sleep.rest.request;

import com.noom.interview.fullstack.sleep.rest.Mood;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SleepDto {

  @NotNull
  LocalTime sleepFrom;
  @NotNull
  LocalTime sleepTo;
  @NotNull
  Mood mood;
}
