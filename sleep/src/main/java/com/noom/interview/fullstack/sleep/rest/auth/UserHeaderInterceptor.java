package com.noom.interview.fullstack.sleep.rest.auth;

import com.noom.interview.fullstack.sleep.model.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserHeaderInterceptor implements HandlerInterceptor {

  private static final String USER_ID = "user-id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String userId = request.getHeader(USER_ID);
    if (NumberUtils.isCreatable(userId)) {
      ContextHolder.setUser(User.builder().id(Long.parseLong(userId)).build());
      return true;
    }
    throw new AuthenticationException();
  }
}
