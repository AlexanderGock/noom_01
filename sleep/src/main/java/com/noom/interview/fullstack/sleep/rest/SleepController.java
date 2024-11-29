package com.noom.interview.fullstack.sleep.rest;

import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDomainToSleepResourceMapper;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDtoToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import com.noom.interview.fullstack.sleep.rest.response.SleepResource;
import com.noom.interview.fullstack.sleep.rest.response.SleepStatistics;
import com.noom.interview.fullstack.sleep.service.ISleepManagementService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SleepController {

  private final ISleepManagementService sleepManagementService;
  private final SleepDtoToSleepDomainMapper sleepDtoToSleepDomainMapper;
  private final SleepDomainToSleepResourceMapper sleepDomainToSleepResourceMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public @Valid SleepResource createSleep(@RequestBody @Valid SleepDto sleepDto) {
    Sleep sleep = sleepDtoToSleepDomainMapper.mapToDomain(sleepDto);
    User hardcodedUser = User.builder().id(8L).build();
    Sleep createdSleep = sleepManagementService.createSleep(sleep, hardcodedUser);
    return sleepDomainToSleepResourceMapper.mapToSleepResource(createdSleep);
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
  public @Valid SleepStatistics getSleeps(@RequestParam(name = "days", required = false, defaultValue = "30") @Min(1) @Max(90) int days) {
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
