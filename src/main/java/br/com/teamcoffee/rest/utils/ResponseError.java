package br.com.teamcoffee.rest.utils;

public class ResponseError {
  public static final ResponseError USER_NOT_FOUND = new ResponseError(401, "Usu�rio n�o encontrado.");
  public static final ResponseError INVALID_PASSWORD = new ResponseError(401, "Senha n�o � v�lida.");
  
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
