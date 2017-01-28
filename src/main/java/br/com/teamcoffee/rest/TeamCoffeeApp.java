package br.com.teamcoffee.rest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import br.com.teamcoffee.config.WebConfig;
import br.com.teamcoffee.service.TeamCoffeeService;

@Configuration
@ComponentScan({"br.com.teamcoffee"})
@EnableMongoRepositories("br.com.teamcoffee")
public class TeamCoffeeApp {  
 
  public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TeamCoffeeApp.class);
    new WebConfig(ctx.getBean(TeamCoffeeService.class));
    ctx.registerShutdownHook();
  }

}