package br.com.teamcoffee.rest.utils;

public class ResponseError {
  public static final ResponseError USER_NOT_FOUND = new ResponseError(
      TeamCoffeeConstants.HttpErrorCode.CONFLICT, TeamCoffeeConstants.ErrorCode.USER_NOT_FOUND, "Usuário não encontrado.");
  public static final ResponseError INVALID_PASSWORD = new ResponseError(
      TeamCoffeeConstants.HttpErrorCode.CONFLICT, TeamCoffeeConstants.ErrorCode.INVALID_PASSWORD, "Senha não é válida.");
  
  private final int httpCode;
  private final String messageKey;
  private final String message;
  private String exceptionMessage;
  
  public ResponseError(int httpCode, String messageKey, String message) {
    this.httpCode = httpCode;
    this.messageKey = messageKey;
    this.message = message;
  }

  public String getMessageKey() {
    return this.messageKey;
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
