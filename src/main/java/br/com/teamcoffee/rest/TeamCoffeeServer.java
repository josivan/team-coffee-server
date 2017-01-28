package br.com.teamcoffee.rest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.teamcoffee.config.WebConfig;
import br.com.teamcoffee.service.TeamCoffeeService;
import spark.servlet.SparkApplication;

public class TeamCoffeeServer implements SparkApplication {

  @Override
  public void init() {
    System.out.println("Starting SparkApplication...");
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TeamCoffeeApp.class);
    new WebConfig(ctx.getBean(TeamCoffeeService.class));
    ctx.registerShutdownHook();
  }

}
