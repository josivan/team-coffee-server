package br.com.teamcoffee.domain;

import org.springframework.data.annotation.Id;

public class User {
  @Id public String id;
  public String name;
  public String email;
  
  @Override
  public String toString() {
    return String.format("User[id=%s, name='%s', email='%s']", id, name, email);
  }
}
