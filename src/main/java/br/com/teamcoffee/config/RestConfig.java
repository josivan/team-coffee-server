package br.com.teamcoffee.config;

import static java.util.Collections.singletonMap;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TeamCoffeeConstants;
import br.com.teamcoffee.rest.utils.TeamCoffeeException;
import br.com.teamcoffee.rest.utils.TeamCoffeeUtils;
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
    setupExceptionsHandlers();
  }
  
  private void setupGets() {
    get("/hello", (req, res) -> "Hello Team Coffee");
    get("/user", (req, res) -> this.userService.findAll(), this.gson::toJson);
    get("/user/:id", (req, res) -> this.userService.findOne(req.params(":id")), this.gson::toJson);
  }
  
  private void setupPosts() {
    post("/login", (req, res) -> {
      JsonObject json = this.teamCoffeeUtils.getAsJsonObject(req.body());
      res.type(TeamCoffeeConstants.APPLICATION_JSON);
      return singletonMap("token", 
        this.userService.doLogin(
          json.get("email").getAsString(), 
          json.get("password").getAsString())); 
    }, this.gson::toJson);
    
    post("/user", (req, res) -> {
      String body = req.body();
      User user = this.gson.fromJson(body, User.class);
      JsonObject json = this.teamCoffeeUtils.getAsJsonObject(body);
      return this.userService.save(user, json.get("password").getAsString());
    }, this.gson::toJson);
  }
  
  private void setupExceptionsHandlers() {
    exception(ConstraintViolationException.class, (e, req, res) -> {
      ConstraintViolationException ex = (ConstraintViolationException) e;
      Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
      List<String> messages = violations.stream()
        .map(c -> String.format("%s %s - [%s]", c.getPropertyPath(), c.getMessage(), c.getInvalidValue()))
        .collect(Collectors.toList());
      res.status(412);
      res.body(this.gson.toJson(singletonMap("messages", messages)));
    });
    exception(DuplicateKeyException.class, (e, req, res) -> {
      
    });
    exception(TeamCoffeeException.class, (e, req, res) -> {
      TeamCoffeeException tce = (TeamCoffeeException) e;
      ResponseError error = tce.getResponseError();
      res.status(error.getHttpCode());
      res.body(this.gson.toJson(error));
    });
  }

  @Override
  public void init() {
    this.setupRoutes();
  }
}
