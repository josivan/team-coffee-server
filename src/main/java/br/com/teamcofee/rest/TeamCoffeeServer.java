package br.com.teamcofee.rest;

import spark.servlet.SparkApplication;

public class TeamCoffeeServer implements SparkApplication {

  @Override
  public void init() {
    new TeamCofeeApp();
  }

}
