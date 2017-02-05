package br.com.teamcoffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.google.gson.Gson;

@Configuration
@ComponentScan({"br.com.teamcoffee"})
@EnableMongoRepositories("br.com.teamcoffee.repositories.mongo")
public class TeamCoffeeConfig {

  @Bean public Gson gson() {
    return new Gson();
  }
}
