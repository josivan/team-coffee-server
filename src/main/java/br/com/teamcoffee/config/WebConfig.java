package br.com.teamcoffee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TeamCoffeeUtils;
import br.com.teamcoffee.rest.utils.TransactionResult;
import br.com.teamcoffee.services.UserService;

public class WebConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);
  private final UserService userService;
  private final TeamCoffeeUtils tcUtil; 
  private Gson gson = new Gson();

  public WebConfig(AbstractApplicationContext springContext) {
    this.userService = springContext.getBean(UserService.class);
    this.tcUtil = springContext.getBean(TeamCoffeeUtils.class);
    this.setupRoutes();
  }

  private void setupRoutes() {
    setupGets();
    setupPosts();
  }
  
  private void setupGets() {
    get("/hello", (req, res) -> "Hello Team Coffee");
    get("/user", (req, res) -> this.userService.findAll(), this.gson::toJson);
    get("/user/:id", (req, res) -> this.userService.findOne(req.params(":id")), this.gson::toJson);
  }
  
  private void setupPosts() {
    post("/user", (req, res) -> {
      String body = req.body();
      User user = this.gson.fromJson(body, User.class);
      JsonObject json = this.tcUtil.getAsJsonObject(body);
      TransactionResult<User> result = this.userService.save(user, json.get("password").getAsString());
      
      if (result.hasError()) {
        ResponseError error = result.getResponseError();
        res.status(error.getHttpCode());
        return error;
      }
      
      return user; 
    }, this.gson::toJson);
  }
}
