package br.com.teamcoffee.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.teamcoffee.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
}
