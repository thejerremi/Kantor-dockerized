package com.konteneryzacja.kantor.admin;

import com.konteneryzacja.kantor.transaction.Transaction;
import com.konteneryzacja.kantor.user.User;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor

public class AdminController {
    private final AdminService service;
    @PutMapping("/roleadmin")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> changeRoleToAdmin(@RequestBody EmailDTO email) {
        service.changeUserRoleToAdmin(email.getEmail());
        return ResponseEntity.ok().body("");
    }
    @PutMapping("/roleuser")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> changeRoleToUser(@RequestBody EmailDTO email) {
        service.changeUserRoleToUser(email.getEmail());
        return ResponseEntity.ok().body("");
    }
    @GetMapping("/transactions")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> findTransactionByUser(@RequestParam String email){
        return ResponseEntity.ok(service.getUserTransactions(email));
    }
    @PutMapping("/currency")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> changeCurrencyRates(@RequestBody CurrencyDTO currency) {
        service.changeCurrencyRates(currency);
        return ResponseEntity.ok().body("");
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class EmailDTO {
    private String email;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class CurrencyDTO{
    private String code;
    private double bid;
    private double ask;
}