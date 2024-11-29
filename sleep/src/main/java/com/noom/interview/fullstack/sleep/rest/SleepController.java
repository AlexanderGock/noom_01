package com.noom.interview.fullstack.sleep.rest;

import com.noom.interview.fullstack.sleep.model.Sleep;
import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.rest.auth.ContextHolder;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDomainToSleepResourceMapper;
import com.noom.interview.fullstack.sleep.rest.mapper.SleepDtoToSleepDomainMapper;
import com.noom.interview.fullstack.sleep.rest.request.SleepDto;
import com.noom.interview.fullstack.sleep.rest.response.SleepResource;
import com.noom.interview.fullstack.sleep.service.ISleepManagementService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sleeps")
@RequiredArgsConstructor
public class SleepController {

  private final ISleepManagementService sleepManagementService;
  private final SleepDtoToSleepDomainMapper sleepDtoToSleepDomainMapper;
  private final SleepDomainToSleepResourceMapper sleepDomainToSleepResourceMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public @Valid SleepResource createSleep(@RequestBody @Valid SleepDto sleepDto) {
    Sleep sleep = sleepDtoToSleepDomainMapper.mapToDomain(sleepDto);
    User user = ContextHolder.getUser();
    Sleep createdSleep = sleepManagementService.createSleep(sleep, user);
    return sleepDomainToSleepResourceMapper.mapToSleepResource(createdSleep);
  }

  @GetMapping("/lastnight")
  public @Valid SleepResource getLastNightSleep() {
    User user = ContextHolder.getUser();
    Sleep sleep = sleepManagementService.getLastNightSleep(user);
    return sleepDomainToSleepResourceMapper.mapToSleepResource(sleep);
  }
}
