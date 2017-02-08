package br.com.teamcoffee.rest.utils;

public class TeamCoffeeException extends RuntimeException {
  private final ResponseError responseError;

  public TeamCoffeeException(ResponseError responseError) {
    this.responseError = responseError;
  }

  public ResponseError getResponseError() {
    return this.responseError;
  }
}
