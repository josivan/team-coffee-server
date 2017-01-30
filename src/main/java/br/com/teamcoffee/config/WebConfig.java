package br.com.teamcoffee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import org.springframework.context.support.AbstractApplicationContext;

import com.google.gson.Gson;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.services.UserService;

public class WebConfig {
  private final UserService userService;
  private Gson gson = new Gson();

  public WebConfig(AbstractApplicationContext springContext) {
    this.userService = springContext.getBean(UserService.class);
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
      this.userService.save(user);
      return user; 
    }, gson::toJson);
  }
}
