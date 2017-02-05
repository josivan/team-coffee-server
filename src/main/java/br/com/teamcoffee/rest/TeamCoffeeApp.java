package br.com.teamcoffee.rest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.teamcoffee.config.RestConfig;
import br.com.teamcoffee.config.TeamCoffeeConfig;

public class TeamCoffeeApp {  
 
  public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TeamCoffeeConfig.class);
    RestConfig app = ctx.getBean(RestConfig.class);
    app.init();
    ctx.registerShutdownHook();
  }

}
