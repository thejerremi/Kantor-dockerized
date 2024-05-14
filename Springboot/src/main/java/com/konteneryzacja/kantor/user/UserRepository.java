package com.konteneryzacja.kantor.user;

import com.mongodb.client.MongoCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);
}
