package com.coforge.dtos;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WalletAmountDto {

    @NotNull
    @Positive
    private BigDecimal amount;
}
