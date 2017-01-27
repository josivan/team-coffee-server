package br.com.teamcofee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import br.com.teamcofee.service.TeamCoffeeService;

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
      System.out.println("Body: " + req.body());
      res.type("application/json");
      return "{\"id\": \"789\"}";
    });
  }
}
