package br.com.teamcoffee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Map;

import com.mongodb.util.JSON;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.service.TeamCoffeeService;
import br.com.teamcoffee.util.TeamCoffeeUtils;

public class WebConfig {
  private TeamCoffeeService service;

  public WebConfig(TeamCoffeeService service) {
    this.service = service;
    this.setupRoutes();
  }
  
  private void setupRoutes() {
    setupGets();
    setupPosts();
  }
  
  private void setupGets() {
    get("/hello", (req, res) -> "Hello Team Coffee");
    get("/user", (req, res) -> {      
      return "{[{name: \'user\'},{name: \'user2\'}]}";
    });
  }
  
  private void setupPosts() {
    post("/user", (req, res) -> {
      User user = createUser(TeamCoffeeUtils.bodyRequestToMap(req));
      this.service.save(user);
      res.type("application/json");
      return String.format("{\"id\": %s }", user.id); 
    });
  }
  
  private User createUser(Map<String, String> data) {
    User user = new User();
    user.name = data.get("name");
    user.email = data.get("email");
    return user;
  }
}
