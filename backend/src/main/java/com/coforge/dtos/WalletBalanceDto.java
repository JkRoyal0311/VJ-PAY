package com.coforge.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletBalanceDto {
    private BigDecimal balance;
}