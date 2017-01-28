package br.com.teamcoffee.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
  @Id public String id;
  @Indexed(unique = true) public String email;
  public String name;
  
  @Override
  public String toString() {
    return String.format("User[id=%s, name='%s', email='%s']", id, name, email);
  }
}
