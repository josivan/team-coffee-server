package br.com.teamcoffee.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.repositories.mongo.UserRepository;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TeamCoffeeUtils;
import br.com.teamcoffee.rest.utils.TransactionResult;

@Service
public class UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  @Autowired private UserRepository userRepository;
  @Autowired private TeamCoffeeUtils teamCoffeeUtils;
  
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
    return this.userRepository.findAll();
  }
  
  public User findOne(String id) {
    User user = this.userRepository.findOne(id);
    LOGGER.info("Senha Válida [{}].", checkPassword(user, "senha"));
    return user;
  }
  
  private boolean checkPassword(User user, String password) {
    return user.getHash().equals(this.teamCoffeeUtils.generateHashAsHexadecimal(user.getSalt(), password));
  }
}
