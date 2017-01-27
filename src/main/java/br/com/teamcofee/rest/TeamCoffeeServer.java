package br.com.teamcofee.rest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.teamcofee.config.WebConfig;
import br.com.teamcofee.service.TeamCoffeeService;
import spark.servlet.SparkApplication;

public class TeamCoffeeServer implements SparkApplication {

  @Override
  public void init() {
    System.out.println("Starting SparkApplication...");
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TeamCofeeApp.class);
    new WebConfig(ctx.getBean(TeamCoffeeService.class));
    ctx.registerShutdownHook();
  }

}
