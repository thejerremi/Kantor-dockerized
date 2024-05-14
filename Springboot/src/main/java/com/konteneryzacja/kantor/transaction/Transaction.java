package com.konteneryzacja.kantor.transaction;

import com.konteneryzacja.kantor.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String exchangedCurrency;
    @DBRef
    @Indexed
    private User user;
}
