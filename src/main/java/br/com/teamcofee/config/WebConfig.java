package br.com.teamcofee.config;

import static spark.Spark.get;

public class WebConfig {

  public WebConfig() {
    this.setupRoutes();
  }
  
  private void setupRoutes() {
    get("/hello", (req, res) -> "Hello Team Coffee");
  }
}
