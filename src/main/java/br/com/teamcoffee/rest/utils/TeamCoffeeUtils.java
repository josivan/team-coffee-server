package br.com.teamcoffee.rest.utils;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;

@Component
public class TeamCoffeeUtils {
  
  public JsonObject getBodyAsJsonObject(Request request) {
    return getAsJsonObject(request.body());
  }
  
  public JsonObject getAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }
}
