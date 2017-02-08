package br.com.teamcoffee.rest.utils;

public interface TeamCoffeeConstants {
  public static final String APPLICATION_JSON = "application/json";
  
  interface HttpErrorCode {
    int CONFLICT = 409;
    int UNAUTHORIZED = 401;
  }
  
  interface ErrorCode {
    int DUPLICATED_EMAIL = 1;
    int USER_NOT_FOUND = 2;
    int INVALID_PASSWORD = 3;
  }
}
