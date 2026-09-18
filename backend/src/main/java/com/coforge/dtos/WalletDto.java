package com.coforge.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.coforge.entities.Beneficiary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WalletDto {
	private Long walletId;
    private BigDecimal balance;
    private List<Beneficiary> beneficiary;
}
