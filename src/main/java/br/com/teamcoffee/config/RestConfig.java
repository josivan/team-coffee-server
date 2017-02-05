package br.com.teamcoffee.config;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TeamCoffeeConstants;
import br.com.teamcoffee.rest.utils.TeamCoffeeUtils;
import br.com.teamcoffee.rest.utils.TransactionResult;
import br.com.teamcoffee.services.UserService;
import spark.servlet.SparkApplication;

@Component
public class RestConfig implements SparkApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(RestConfig.class);

  @Autowired private Gson gson;
  @Autowired private UserService userService;
  @Autowired private TeamCoffeeUtils teamCoffeeUtils;
  
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
    post("/login", (req, res) -> {
      JsonObject json = this.teamCoffeeUtils.getAsJsonObject(req.body());
      TransactionResult<String> result = this.userService.doLogin(
          json.get("email").getAsString(),
          json.get("password").getAsString());
      res.type(TeamCoffeeConstants.APPLICATION_JSON);
      
      if (result.hasError()) {
        ResponseError error = result.getResponseError();
        res.status(error.getHttpCode());
        return error;
      }

      return Collections.singletonMap("token", result.getEntity()); 
    }, this.gson::toJson);
    
    post("/user", (req, res) -> {
      String body = req.body();
      User user = this.gson.fromJson(body, User.class);
      JsonObject json = this.teamCoffeeUtils.getAsJsonObject(body);
      TransactionResult<User> result = this.userService.save(user, json.get("password").getAsString());
      
      if (result.hasError()) {
        ResponseError error = result.getResponseError();
        res.status(error.getHttpCode());
        return error;
      }
      
      return user; 
    }, this.gson::toJson);
  }

  @Override
  public void init() {
    this.setupRoutes();
  }
}
