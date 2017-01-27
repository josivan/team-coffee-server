package br.com.teamcofee.rest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.teamcofee.config.WebConfig;
import br.com.teamcofee.service.TeamCoffeeService;

@Configuration
@ComponentScan({"br.com.teamcofee"})
public class TeamCofeeApp {  
 
  public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TeamCofeeApp.class);
    new WebConfig(ctx.getBean(TeamCoffeeService.class));
    ctx.registerShutdownHook();
  }

}
