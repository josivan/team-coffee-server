package br.com.teamcoffee.services;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.repositories.mongo.UserRepository;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TeamCoffeeUtils;
import br.com.teamcoffee.rest.utils.TransactionResult;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  private static final String KEY = "team-coffee";
  
  @Autowired private UserRepository userRepository;
  @Autowired private TeamCoffeeUtils teamCoffeeUtils;
  @Autowired private Gson gson;
  @Autowired private MongoTemplate mongoTemplate;
  
  public TransactionResult<User> save(User user, String password) {
    TransactionResult<User> result = new TransactionResult<>();
    try {
      String salt = this.teamCoffeeUtils.generateSaltAsHexadecimal();
      user.setSalt(salt);
      user.setHash(this.teamCoffeeUtils.generateHashAsHexadecimal(salt, password));
      this.userRepository.save(user);
      result.setEntity(user);
    }
    catch (DuplicateKeyException e) {
      LOGGER.error("Error on saveUser.", e);
      ResponseError error = new ResponseError(409, String.format("Já existe um usuário com o email %s.", user.getEmail()));
      result.setResponseError(error);
    }
    return result;
  }
  
  public List<User> findAll() {
    Aggregation agg = newAggregation(User.class, project("id", "name", "email"));
    AggregationResults<User> result = this.mongoTemplate.aggregate(agg, "users", User.class);
    return result.getMappedResults();
  }
  
  public User findOne(String id) {
    User user = this.userRepository.findOne(id);
    LOGGER.info("Senha Válida [{}].", checkPassword(user, "senha"));
    return user;
  }
  
  public TransactionResult<String> doLogin(String email, String password) {
    TransactionResult<String> result = new TransactionResult<>();
    User user = this.findByEmail(email);
    
    if (user == null) {
      result.setResponseError(ResponseError.USER_NOT_FOUND);
    }
    else if (!this.checkPassword(user, password)) {
      result.setResponseError(ResponseError.INVALID_PASSWORD);
    }
    else {
      Map<String, String> data = new HashMap<>();
      data.put("email", user.getEmail());
      data.put("id", user.getId());
      data.put("name", user.getName());
      
      byte[] key = KEY.getBytes();
      Date expirationDate = new Date();
      result.setEntity(Jwts.builder()
        .setSubject(this.gson.toJson(data))
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS256, key)
        .compact());
    }
    
    return result;
  }
  
  private boolean checkPassword(User user, String password) {
    return user.getHash().equals(this.teamCoffeeUtils.generateHashAsHexadecimal(user.getSalt(), password));
  }
  
  private User findByEmail(String email) {
    User u = new User();
    u.setEmail(email);
    Example<User> example = Example.of(u);
    return this.userRepository.findOne(example);
  }
}
