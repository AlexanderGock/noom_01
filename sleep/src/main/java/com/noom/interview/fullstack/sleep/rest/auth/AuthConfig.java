package com.noom.interview.fullstack.sleep.rest.auth;

import com.noom.interview.fullstack.sleep.db.mapper.UserEntityToUserDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

  private final UserRepository userRepository;
  private final UserEntityToUserDomainMapper userEntityToUserDomainMapper;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new UserHeaderInterceptor(userRepository, userEntityToUserDomainMapper))
        .addPathPatterns("/api/**");
  }
}
