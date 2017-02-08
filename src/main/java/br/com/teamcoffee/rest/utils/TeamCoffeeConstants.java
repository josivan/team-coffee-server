package br.com.teamcoffee.rest.utils;

public interface TeamCoffeeConstants {
  public static final String APPLICATION_JSON = "application/json";
  
  interface HttpErrorCode {
    int CONFLICT = 409;
    int UNAUTHORIZED = 401;
  }
  
  interface ErrorCode {
    String DUPLICATED_EMAIL = "duplicatedEmail";
    String USER_NOT_FOUND = "userNotFound";
    String INVALID_PASSWORD = "invalidPassword";
  }
}
