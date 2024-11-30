package com.noom.interview.fullstack.sleep.rest.auth;

import com.noom.interview.fullstack.sleep.model.User;

public class ContextHolder {

  private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

  public static User getUser() {
    return userHolder.get();
  }

  public static void setUser(User user) {
    userHolder.set(user);
  }
}
