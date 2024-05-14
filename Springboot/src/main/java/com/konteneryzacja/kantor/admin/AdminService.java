package com.konteneryzacja.kantor.admin;

import com.konteneryzacja.kantor.currency.CurrencyRate;
import com.konteneryzacja.kantor.currency.CurrencyRateRepository;
import com.konteneryzacja.kantor.currency.CurrencyRateService;
import com.konteneryzacja.kantor.transaction.TransactionResponse;
import com.konteneryzacja.kantor.transaction.TransactionService;
import com.konteneryzacja.kantor.user.Role;
import com.konteneryzacja.kantor.user.User;
import com.konteneryzacja.kantor.user.UserRepository;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final CurrencyRateService currencyRateService;
    private final CurrencyRateRepository currencyRateRepository;
    public void changeUserRoleToAdmin(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            user.get().setRole(Role.valueOf("ADMIN"));
            userRepository.save(user.get());
        }
    }
    public void changeUserRoleToUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            user.get().setRole(Role.valueOf("USER"));
            userRepository.save(user.get());
        }
    }
    public List<TransactionResponse> getUserTransactions(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return transactionService.findTransactionByUser(user.get());
        }
        return List.of();
    }
    public void changeCurrencyRates(CurrencyDTO currency){
        Optional<CurrencyRate> changedCurrency = currencyRateService.findByCode(currency.getCode());
        if(changedCurrency.isPresent()){
            changedCurrency.get().setAsk(currency.getAsk());
            changedCurrency.get().setBid(currency.getBid());
            currencyRateRepository.save(changedCurrency.get());
        }
    }
}


