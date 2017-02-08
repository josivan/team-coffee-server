package br.com.teamcoffee.rest.utils;

public class ResponseError {
  public static final ResponseError USER_NOT_FOUND = new ResponseError(
      TeamCoffeeConstants.HttpErrorCode.CONFLICT, TeamCoffeeConstants.ErrorCode.USER_NOT_FOUND, "Usuário não encontrado.");
  public static final ResponseError INVALID_PASSWORD = new ResponseError(
      TeamCoffeeConstants.HttpErrorCode.CONFLICT, TeamCoffeeConstants.ErrorCode.INVALID_PASSWORD, "Senha não é válida.");
  
  private final int httpCode;
  private final int errorCode;
  private final String message;
  private String exceptionMessage;
  
  public ResponseError(int httpCode, int errorCode, String message) {
    this.httpCode = httpCode;
    this.errorCode = errorCode;
    this.message = message;
  }

  public int getErrorCode() {
    return this.errorCode;
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
