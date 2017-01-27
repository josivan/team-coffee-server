package br.com.teamcofee.rest;

import static spark.Spark.*;

import spark.servlet.SparkApplication;

public class TeamCoffeeServer implements SparkApplication {

  @Override
  public void init() {
    System.out.println("Iniciando rotas...");
    get("/hello", (req, res) -> "Hello World");
  }

}
