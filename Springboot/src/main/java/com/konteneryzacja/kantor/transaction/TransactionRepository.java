package com.konteneryzacja.kantor.transaction;

import com.konteneryzacja.kantor.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findTransactionByUser(User user);
}
