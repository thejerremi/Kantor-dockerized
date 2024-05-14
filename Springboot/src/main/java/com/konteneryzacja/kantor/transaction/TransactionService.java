package com.konteneryzacja.kantor.transaction;

import com.konteneryzacja.kantor.user.User;
import com.konteneryzacja.kantor.user.UserRepository;
import com.konteneryzacja.kantor.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.konteneryzacja.kantor.transaction.TransactionType.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final UserService userService;

    @Cacheable("transactions")
    public List<TransactionResponse> findTransactionByUser(User user) {
        return repository.findTransactionByUser(user).stream()
                .map(t -> TransactionResponse.builder()
                        .type(t.getType())
                        .createdAt(t.getCreatedAt())
                        .amount(t.getAmount())
                        .exchangedCurrency(t.getExchangedCurrency())
                        .build())
                .collect(Collectors.toList());
    }
    public void deposit(Transaction request, User user){
        var deposit = Transaction.builder()
                .type(DEPOSIT)
                .amount(request.getAmount())
                .user(user)
                .createdAt(LocalDateTime.now(ZoneId.of("UTC+2")))
                .exchangedCurrency("PLN")
                .build();
        userService.addBalance(user, request.getAmount());
        repository.save(deposit);
    }
    public void sell(Transaction request, User user){
        var deposit = Transaction.builder()
                .type(SELL)
                .amount(request.getAmount())
                .user(user)
                .createdAt(LocalDateTime.now(ZoneId.of("UTC+2")))
                .exchangedCurrency(request.getExchangedCurrency())
                .build();
        userService.addBalance(user, request.getAmount());
        repository.save(deposit);
    }
    public void withdraw(Transaction request, User user){
        var withdraw = Transaction.builder()
                .type(WITHDRAW)
                .amount(request.getAmount().negate())
                .user(user)
                .createdAt(LocalDateTime.now(ZoneId.of("UTC+2")))
                .exchangedCurrency("PLN")
                .build();
        userService.subtractBalance(user, request.getAmount());
        repository.save(withdraw);
    }

    public void buy(Transaction request, User user) {
        var buy = Transaction.builder()
                .type(BUY)
                .amount(request.getAmount().negate())
                .user(user)
                .createdAt(LocalDateTime.now(ZoneId.of("UTC+2")))
                .exchangedCurrency(request.getExchangedCurrency())
                .build();
        userService.subtractBalance(user, request.getAmount());
        repository.save(buy);
    }
}
