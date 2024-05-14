package com.konteneryzacja.kantor.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String firstname;
    private String lastname;
    private String email;
    private BigDecimal balance;
    private Role role;

}
