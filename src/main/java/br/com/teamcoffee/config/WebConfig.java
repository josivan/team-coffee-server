package br.com.teamcoffee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.service.TeamCoffeeService;

public class WebConfig {
  private TeamCoffeeService service;
  private Gson gson = new Gson();

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
      User user = gson.fromJson(req.body(), User.class);
      this.service.save(user);
      return user; 
    }, gson::toJson);
  }
}
