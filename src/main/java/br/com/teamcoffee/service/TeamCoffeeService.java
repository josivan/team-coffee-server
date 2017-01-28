package br.com.teamcoffee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.teamcoffee.domain.User;
import br.com.teamcoffee.repository.UserRepository;

@Service
public class TeamCoffeeService {
  @Autowired private UserRepository userRepository;
  
  public void save(User user) {
    this.userRepository.save(user);
  }
}
