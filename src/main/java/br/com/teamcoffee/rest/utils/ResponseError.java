package br.com.teamcoffee.rest.utils;

public class ResponseError {
  private final int httpCode;
  private final String message;
  private String exceptionMessage;
  
  public ResponseError(int httpCode, String message) {
    this.httpCode = httpCode;
    this.message = message;
  }

  public int getHttpCode() {
    return this.httpCode;
  }

  public String getExceptionMessage() {
    return this.exceptionMessage;
  }

  public void setExceptionMessage(String exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }
}
