package com.konteneryzacja.kantor.currency;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CurrencyRate {
    @Id
    private String id;

    private String code;
    private double bid;
    private double ask;

    // Constructors, getters, and setters
    public CurrencyRate() {}

    public CurrencyRate(String code, double bid, double ask) {
        this.code = code;
        this.bid = bid;
        this.ask = ask;
    }

    // standard getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }
}
