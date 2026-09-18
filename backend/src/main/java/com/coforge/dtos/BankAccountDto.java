package com.coforge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccountDto {
	private long bankAccountId;
	private String accountNo;
	private String ifscCode;
	private String bankname;
	private double balance;
}
