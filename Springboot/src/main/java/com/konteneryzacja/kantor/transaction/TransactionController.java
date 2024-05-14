package com.konteneryzacja.kantor.transaction;

import com.konteneryzacja.kantor.auth.AuthenticationService;
import com.konteneryzacja.kantor.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;
    private final AuthenticationService authService;
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Transaction request, @RequestHeader("Authorization") String token){
        Optional<User> user = authService.findUserByToken(token);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("User wasn't found or access token is invalid.");
        }
        service.deposit(request, user.get());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody Transaction request, @RequestHeader("Authorization") String token){
        Optional<User> user = authService.findUserByToken(token);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("User wasn't found or access token is invalid.");
        }
        service.sell(request, user.get());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Transaction request, @RequestHeader("Authorization") String token){
        Optional<User> user = authService.findUserByToken(token);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("User wasn't found or access token is invalid.");
        }
        if(user.get().getBalance().compareTo(request.getAmount()) < 0){
            return ResponseEntity.badRequest().body("Insufficient balance.");
        }
        service.withdraw(request, user.get());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/buy")
    public ResponseEntity<?> buy(@RequestBody Transaction request, @RequestHeader("Authorization") String token){
        Optional<User> user = authService.findUserByToken(token);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("User wasn't found or access token is invalid.");
        }
        if(user.get().getBalance().compareTo(request.getAmount()) < 0){
            return ResponseEntity.badRequest().body("Insufficient balance.");
        }
        service.buy(request, user.get());
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<?> findTransactionByUser(@RequestHeader("Authorization") String token){
        Optional<User> user = authService.findUserByToken(token);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("User wasn't found or access token is invalid.");
        }
        return ResponseEntity.ok(service.findTransactionByUser(user.get()));
    }
}
