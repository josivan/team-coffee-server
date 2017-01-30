package br.com.teamcoffee.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.repositories.mongo.UserRepository;
import br.com.teamcoffee.rest.utils.ResponseError;
import br.com.teamcoffee.rest.utils.TransactionResult;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;
  
  public TransactionResult<User> save(User user) {
    TransactionResult<User> result = new TransactionResult<>();
    try {
      this.userRepository.save(user);
      result.setEntity(user);
    }
    catch (DuplicateKeyException e) {
      ResponseError error = new ResponseError(409, String.format("Já existe um usuário com o email %s.", user.email));
      // error.setExceptionMessage(e.getLocalizedMessage());
      result.setResponseError(error);
    }
    return result;
  }
  
  public List<User> findAll() {
    return this.userRepository.findAll();
  }
}
