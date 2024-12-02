package com.noom.interview.fullstack.sleep.rest.auth;

import com.noom.interview.fullstack.sleep.db.entity.UserEntity;
import com.noom.interview.fullstack.sleep.db.mapper.UserEntityToUserDomainMapper;
import com.noom.interview.fullstack.sleep.db.repository.UserRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class UserHeaderInterceptor implements HandlerInterceptor {

  private static final String USER_ID = "user-id";

  private final UserRepository userRepository;
  private final UserEntityToUserDomainMapper userEntityToUserDomainMapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String userId = request.getHeader(USER_ID);
    if (NumberUtils.isCreatable(userId)) {
      Optional<UserEntity> userEntityOptional = userRepository.findById(Long.parseLong(userId));
      if (userEntityOptional.isPresent()) {
        ContextHolder.setUser(
            userEntityToUserDomainMapper.mapToDomain(userEntityOptional.get()));
        return true;
      }
    }
    throw new AuthenticationException();
  }
}
