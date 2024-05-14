package com.konteneryzacja.kantor.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

  @Query("{ 'user.$id': ?0, '$or': [ { 'expired': false }, { 'revoked': false } ] }")
  List<Token> findAllValidTokenByUser(String userId);

  Optional<Token> findByToken(String token);
}
