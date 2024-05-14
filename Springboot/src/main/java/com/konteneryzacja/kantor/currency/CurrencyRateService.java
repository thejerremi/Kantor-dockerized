package com.konteneryzacja.kantor.currency;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CurrencyRateService {
    private final RestTemplate restTemplate;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateService(CurrencyRateRepository currencyRateRepository) {
        this.restTemplate = new RestTemplate();
        this.currencyRateRepository = currencyRateRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Cron expression for once a day at midnight
    public void fetchDataDaily() {
        fetchData();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fetchDataOnStartup() {
        fetchData();
    }

    private void fetchData() {
        currencyRateRepository.deleteAll();
        String url = "https://api.nbp.pl/api/exchangerates/tables/C/";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> dataList = response.getBody();
            assert dataList != null;
            if (!dataList.isEmpty()) {
                List<Map<String, Object>> rates = (List<Map<String, Object>>) dataList.get(0).get("rates");
                rates.forEach(rate -> {
                    CurrencyRate currencyRate = new CurrencyRate(
                            (String) rate.get("code"),
                            (Double) rate.get("bid"),
                            (Double) rate.get("ask")
                    );
                    currencyRateRepository.save(currencyRate);
                });
            }
        }
    }
    public List<CurrencyRate> findAll(){
        return currencyRateRepository.findAll();
    }
    public Optional<CurrencyRate> findByCode(String code){
        return currencyRateRepository.findByCode(code);
    }

}
