package com.coforge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityBillPaymentDto {
	private String state;
	private String billerName;
	private String accountNumber;
}
