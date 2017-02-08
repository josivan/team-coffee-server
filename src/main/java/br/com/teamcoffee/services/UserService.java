package br.com.teamcoffee.services;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

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
import br.com.teamcoffee.rest.utils.TeamCoffeeConstants;
import br.com.teamcoffee.rest.utils.TeamCoffeeException;
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
  
  public User save(User user, String password) {
    try {
      String salt = this.teamCoffeeUtils.generateSaltAsHexadecimal();
      user.setSalt(salt);
      user.setHash(this.teamCoffeeUtils.generateHashAsHexadecimal(salt, password));
      return this.userRepository.save(user);
    }
    /*
    catch (ConstraintViolationException e) {
      LOGGER.error("Error on saveUser.", e);
      ResponseError error = new ResponseError(412, String.format("Validação Falhou: %s.", e.getMessage()));
      result.setResponseError(error);
    }
    */
    catch (DuplicateKeyException e) {
      LOGGER.error("Error on saveUser.", e);
      throw new TeamCoffeeException(new ResponseError(
          TeamCoffeeConstants.HttpErrorCode.CONFLICT, 
          TeamCoffeeConstants.ErrorCode.DUPLICATED_EMAIL, 
          String.format("Já existe um usuário com o email %s.", user.getEmail())));
    }
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
  
  public String doLogin(String email, String password) {
    User user = this.findByEmail(email);
    
    if (user == null) {
      throw new TeamCoffeeException(ResponseError.USER_NOT_FOUND);
    }
    
    if (!this.checkPassword(user, password)) {
      throw new TeamCoffeeException(ResponseError.INVALID_PASSWORD);
    }
    
    Map<String, String> data = new HashMap<>();
    data.put("email", user.getEmail());
    data.put("id", user.getId());
    data.put("name", user.getName());
      
    byte[] key = KEY.getBytes();
    Date expirationDate = new Date();
    
    return Jwts.builder()
      .setSubject(this.gson.toJson(data))
      .setExpiration(expirationDate)
      .signWith(SignatureAlgorithm.HS256, key)
      .compact();
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
