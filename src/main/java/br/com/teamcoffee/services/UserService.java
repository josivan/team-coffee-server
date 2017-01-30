package br.com.teamcoffee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.repositories.mongo.UserRepository;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;
  
  public boolean save(User user) {
    try {
      this.userRepository.save(user);
      return true;
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
