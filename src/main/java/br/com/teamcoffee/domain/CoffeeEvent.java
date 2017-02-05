package br.com.teamcoffee.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@TypeAlias("event")
public class CoffeeEvent {
  @Id private String id;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
