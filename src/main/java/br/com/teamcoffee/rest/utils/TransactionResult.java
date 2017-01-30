package br.com.teamcoffee.rest.utils;

public class TransactionResult<E> {
  private E entity;
  private ResponseError responseError;

  public void setEntity(E entity) {
    this.entity = entity;
  }
  
  public boolean hasError() {
    return this.responseError != null;
  }
  
  public E getEntity() {
    return this.entity;
  }

  public ResponseError getResponseError() {
    return this.responseError;
  }

  public void setResponseError(ResponseError responseError) {
    this.responseError = responseError;
  }
}
