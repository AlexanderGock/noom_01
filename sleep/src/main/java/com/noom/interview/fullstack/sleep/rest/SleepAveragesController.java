package com.noom.interview.fullstack.sleep.rest;

import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepStatisticsDomainToRestMapper;
import com.noom.interview.fullstack.sleep.rest.response.SleepStatistics;
import com.noom.interview.fullstack.sleep.service.ISleepAveragesService;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sleeps/averages")
@RequiredArgsConstructor
@Validated
public class SleepAveragesController {

  private final ISleepAveragesService sleepAveragesService;
  private final SleepStatisticsDomainToRestMapper sleepStatisticsDomainToRestMapper;

  @GetMapping
  public @Valid SleepStatistics getSleepAverages(@RequestParam(required = false, defaultValue = "30") @Min(1) @Max(90) int days) {
    User hardcodedUser = User.builder().id(12L).build();
    com.noom.interview.fullstack.sleep.model.SleepStatistics sleepStatistics =
        sleepAveragesService.getAverageValues(hardcodedUser, days);
    return sleepStatisticsDomainToRestMapper.mapToRest(sleepStatistics);
  }
}
