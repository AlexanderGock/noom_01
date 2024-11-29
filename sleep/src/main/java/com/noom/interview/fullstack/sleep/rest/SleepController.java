package com.noom.interview.fullstack.sleep.rest;

import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import com.noom.interview.fullstack.sleep.rest.response.SleepResource;
import com.noom.interview.fullstack.sleep.rest.response.SleepStatistics;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sleeps")
@Slf4j
public class SleepController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public @Valid SleepResource createSleep(@RequestBody @Valid SleepDto sleepDto) {
    log.info("sleep: {}", sleepDto);
    return SleepResource.builder()
        .sleepTo(LocalTime.now())
        .sleepFrom(LocalTime.now())
        .sleepDay(LocalDate.now())
        .duration(LocalTime.of(8, 5))
        .mood(Mood.OK)
        .build();
  }

  @GetMapping("/lastnight")
  public @Valid SleepResource getLastNightSleep() {
    return SleepResource.builder()
        .sleepTo(LocalTime.now())
        .sleepFrom(LocalTime.now())
        .sleepDay(LocalDate.now())
        .duration(LocalTime.of(8, 5))
        .mood(Mood.OK)
        .build();
  }

  @GetMapping
  public @Valid SleepStatistics getSleeps(@RequestParam(name = "days", required = false, defaultValue = "30") int days) {
    return SleepStatistics.builder()
        .dayFrom(LocalDate.now())
        .dayTo(LocalDate.now())
        .averageTimeFrom(LocalTime.now())
        .averageTimeTo(LocalTime.now())
        .averageDuration(LocalTime.now())
        .moodDistribution(new HashMap<>())
        .build();
  }
}
