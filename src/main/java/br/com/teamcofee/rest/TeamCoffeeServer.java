package br.com.teamcofee.rest;

import br.com.teamcofee.config.WebConfig;
import spark.servlet.SparkApplication;

public class TeamCoffeeServer implements SparkApplication {

  @Override
  public void init() {
    System.out.println("Iniciando rotas...");
    new WebConfig();
  }

}
