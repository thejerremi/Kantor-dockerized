package com.konteneryzacja.kantor.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final CurrencyRateService service;
    @GetMapping
    public ResponseEntity<List<CurrencyRate>> findAllCurrencyRates(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping(value = "/{code}")
    public Optional<CurrencyRate> findByCode(@PathVariable("code") String code){
        return ResponseEntity.ok(service.findByCode(code)).getBody();
    }
}
