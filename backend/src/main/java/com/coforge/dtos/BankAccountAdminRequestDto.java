package com.coforge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccountAdminRequestDto {
	private long bankAccountId;
	private String accountNo;
	private String ifscCode;
	private String bankname;
	private double balance;
	private long custId;
	
	public BankAccountAdminRequestDto(String accountNo, String ifscCode, String bankname, double balance, long custId) {
		super();
		this.accountNo = accountNo;
		this.ifscCode = ifscCode;
		this.bankname = bankname;
		this.balance = balance;
		this.custId = custId;
	}
}
