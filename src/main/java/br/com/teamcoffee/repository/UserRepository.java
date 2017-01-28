package br.com.teamcoffee.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.teamcoffee.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

}
