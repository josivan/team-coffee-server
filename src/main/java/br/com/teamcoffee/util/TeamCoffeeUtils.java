package br.com.teamcoffee.util;

import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import spark.Request;

public class TeamCoffeeUtils {
  
  @SuppressWarnings("unchecked")
  public static Map<String, String> bodyRequestToMap(Request req) {
    return ((BasicDBObject) JSON.parse(req.body())).toMap();
  }
}
