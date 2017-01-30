package br.com.teamcoffee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import org.springframework.context.support.AbstractApplicationContext;

import com.google.gson.Gson;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TransactionResult;
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
    get("/user", (req, res) -> this.userService.findAll(), this.gson::toJson);
  }
  
  private void setupPosts() {
    post("/user", (req, res) -> {
      User user = this.gson.fromJson(req.body(), User.class);
      TransactionResult<User> result = this.userService.save(user);
      
      if (result.hasError()) {
        ResponseError error = result.getResponseError();
        res.status(error.getHttpCode());
        return error;
      }
      return user; 
    }, this.gson::toJson);
  }
}
